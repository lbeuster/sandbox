package de.lbe.sandbox.firebase;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Verify;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.utilities.DefaultRunLoop;

import de.asideas.ipool.commons.lib.lang.reflect.MoreReflection;
import de.lbe.sandbox.firebase.logging.Slf4jLogger;

/**
 * Seems like a free firebase instance has a limited throughput of 5 messages/sec (300/min) AND 50 k/sec (3 MB/min).
 *
 * @author lbeuster
 */
public class FirebaseClient {

	private final FirebaseDatabase database;

	private final DatabaseConfig config;

	private final SyncQuerySupport syncQueries = new SyncQuerySupport();

	private boolean started = false;

	public FirebaseClient(FirebaseOptions options, String name) {
		this(FirebaseApp.initializeApp(options, name));
		config.setLogger(new Slf4jLogger());
	}

	public FirebaseClient(FirebaseOptions options) {
		this(FirebaseApp.initializeApp(options));
		config.setLogger(new Slf4jLogger());
	}

	public FirebaseClient(FirebaseApp app) {
		this.database = FirebaseDatabase.getInstance(app);
		try {
			this.config = (DatabaseConfig) MoreReflection.invokePrivilegedInstanceMethod(this.database, "getConfig");
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public DatabaseReference getReference(String path) {
		startIfNecessary();
		return database.getReference(path);
	}

	public DatabaseReference getReference() {
		startIfNecessary();
		return database.getReference();
	}

	public FirebaseDatabase getDatabase() {
		return this.database;
	}

	public void awaitCompletion() throws InterruptedException, TimeoutException {
		syncQueries().removeValue(getReference(UUID.randomUUID().toString()));
	}

	public void awaitFirstCompletion() throws InterruptedException, TimeoutException {
		awaitCompletion(10_000);
	}

	public void awaitCompletion(long timeoutInMillis) throws InterruptedException, TimeoutException {
		syncQueries().removeValue(getReference(UUID.randomUUID().toString()), timeoutInMillis);
	}

	/**
	 * We need this method to ensure that all internal fields are initialized (e.g. executor service).
	 */
	public void startIfNecessary() {
		if (!this.started) {

			// BTW: it's not possible to increase the thread pool size of firebase because the results in concurrency issues with the websocket connection

			// calling this method ensures that everything is fine
			database.getReference();
			this.started = true;
		}
	}

	public SyncQuerySupport syncQueries() {
		startIfNecessary();
		return this.syncQueries;
	}

	public DatabaseConfig getConfig() {
		return this.config;
	}

	public ScheduledThreadPoolExecutor getExecutor() {
		DefaultRunLoop runLoop = (DefaultRunLoop) getConfig().getRunLoop();
		Verify.verifyNotNull(runLoop, "firebase client not initialized");
		return (ScheduledThreadPoolExecutor) runLoop.getExecutorService();
	}
}
