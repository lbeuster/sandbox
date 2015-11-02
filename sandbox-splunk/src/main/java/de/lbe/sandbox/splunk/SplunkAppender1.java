package de.lbe.sandbox.splunk;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.net.SocketFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.net.DefaultSocketConnector;
import ch.qos.logback.core.net.SocketConnector;
import ch.qos.logback.core.util.CloseUtil;

/**
 * Logback Appender which writes its events to a TCP port.
 *
 * This class is based on the logic of Logback's SocketAppender, but does not try to serialize Java objects for deserialization and logging elsewhere.
 */
@SuppressWarnings({ "unqualified-field-access", "null" })
public class SplunkAppender1 extends AppenderBase<ILoggingEvent>implements Runnable, SocketConnector.ExceptionHandler {
	private static final int DEFAULT_RECONNECTION_DELAY = 30000; // in ms
	private static final int DEFAULT_QUEUE_SIZE = 100;
	private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;

	private String host;
	private int port;
	private InetAddress address;

	private Layout<ILoggingEvent> layout;
	private Layout<ILoggingEvent> errorLayout;
	private Future<?> task;
	private Future<Socket> connectorTask;

	private int reconnectionDelay = DEFAULT_RECONNECTION_DELAY;
	private int queueSize = DEFAULT_QUEUE_SIZE;
	private int acceptConnectionTimeout = DEFAULT_ACCEPT_CONNECTION_DELAY;

	private BlockingQueue<ILoggingEvent> queue;

	// The socket will be modified by the another thread (in SocketConnector) which
	// handles reconnection of dropped connections.
	private volatile Socket socket;

	// The appender is created by Logback calling a superclass constructor with no arguments.
	// Then it calls setters (and the setters defined by the class define what arguments are
	// understood. Once all the fields have been set, Logback calls start(), When shutting down,
	// Logback calls stop().
	//
	// start() queues the appender as a Runnable, so run() eventually gets invoked to do the
	// actual work. run() opens a port using Logback utilities that reconnect when a connection
	// is lost, and then block on a queue of events, writing them to TCP as soon as they
	// become available.
	//
	// The append method, which Logback logging calls invoke, pushes events to that queue and nothing else.

	@Override
	public void connectionFailed(SocketConnector socketConnector, Exception e) {
		if (e instanceof InterruptedException) {
			addInfo("connector interrupted");
		} else if (e instanceof ConnectException) {
			addInfo(host + ":" + port + " connection refused");
		} else {
			addInfo(host + ":" + port + " " + e);
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				SocketConnector connector = new DefaultSocketConnector(address, port, 0, reconnectionDelay);
				connector.setExceptionHandler(this);
				connector.setSocketFactory(SocketFactory.getDefault());

				try {
					connectorTask = getContext().getExecutorService().submit(connector);
				} catch (RejectedExecutionException e) {
					connectorTask = null;
					break;
				}

				try {
					socket = connectorTask.get();
					connectorTask = null;
				} catch (ExecutionException e) {
					socket = null;
					break;
				}

				try {
					socket.setSoTimeout(acceptConnectionTimeout);
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
					socket.setSoTimeout(0);
					socket.setSendBufferSize(128);

					addInfo(host + ":" + port + " connection established");

					while (true) {
						ILoggingEvent event = queue.take();
						writeEvent(writer, event);
					}
				} catch (SocketException e) {
					System.err.println("exception in logging " + e);
					addInfo(host + ":" + port + " connection failed: " + e);
				} catch (IOException e) {
					CloseUtil.closeQuietly(socket);
					socket = null;
					addInfo(host + ":" + port + " connection closed");
				}
			}
		} catch (InterruptedException ex) {
			// Exiting.
		}
		addInfo("shutting down");
	}

	/**
	 *
	 */
	protected void writeEvent(OutputStreamWriter writer, ILoggingEvent event) throws IOException {
		String formatted = selectLayout(event).doLayout(event);
		writer.write(formatted);
		writer.flush();
	}

	/**
	 *
	 */
	private Layout<ILoggingEvent> selectLayout(ILoggingEvent event) {

		// if we only have one
		if (this.errorLayout == null) {
			return this.layout;
		}

		// select layout depending on level
		int level = event.getLevel().toInt();
		return level >= Level.ERROR_INT ? this.errorLayout : this.layout;
	}

	@Override
	public void start() {
		if (started) {
			return;
		}

		boolean errorPresent = false;

		// Handle options
		if (port <= 0) {
			errorPresent = true;
			addError("No port was configured for appender" + name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_port");
		}

		if (host == null) {
			errorPresent = true;
			addError("No remote host was configured for appender" + name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_host");
		}

		if (queueSize < 0) {
			errorPresent = true;
			addError("Queue size must be non-negative");
		}

		if (this.layout == null) {
			addError("No layout set for the appender named [" + name + "].");
			errorPresent = true;
		}

		if (!errorPresent) {
			try {
				address = InetAddress.getByName(host);
			} catch (UnknownHostException ex) {
				addError("unknown host: " + host);
				errorPresent = true;
			}
		}

		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			addError("Unknown host: " + host);
			errorPresent = true;
		}

		// Dispatch this instance of the appender.
		if (!errorPresent) {
			queue = new ArrayBlockingQueue<>(queueSize);
			task = getContext().getExecutorService().submit(this);
		}

		super.start();
	}

	@Override
	public void stop() {
		if (!started)
			return;

		CloseUtil.closeQuietly(socket);
		task.cancel(true);
		if (connectorTask != null) {
			connectorTask.cancel(true);
		}
		super.stop();
	}

	@Override
	protected void append(ILoggingEvent event) {

		// ensure that caller data (thread, stacktrace) of the logging thread are initialized
		event.prepareForDeferredProcessing();
		if (event != null && started) {
			queue.offer(event);
		}
	}

	@Override
	public void addError(String msg) {
		System.err.println(msg);
		super.addError(msg);
	}

	// The setters are peculiar here. They are used by Logback (via reflection) to set
	// the parameters of the appender, but they should never be called except by
	// Logback before start() is called.
	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

	public void setReconnectionDelay(int reconnectionDelay) {
		this.reconnectionDelay = reconnectionDelay;
	}

	public int getReconnectionDelay() {
		return this.reconnectionDelay;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public int getQueueSize() {
		return this.queueSize;
	}

	public void setLayout(Layout<ILoggingEvent> layout) {
		this.layout = layout;
	}

	public Layout<ILoggingEvent> getLayout() {
		return this.layout;
	}

	public Layout<ILoggingEvent> getErrorLayout() {
		return errorLayout;
	}

	@DefaultClass(PatternLayout.class)
	public void setErrorLayout(Layout<ILoggingEvent> errorLayout) {
		this.errorLayout = errorLayout;
	}
}
