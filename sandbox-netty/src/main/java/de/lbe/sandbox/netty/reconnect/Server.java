package de.lbe.sandbox.netty.reconnect;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final int PORT = 34567;

    private ServerSocket serverSock;

    public Server(int port) throws IOException {
        serverSock = new ServerSocket(port);
    }

    public void start() throws Exception {
        while (true) {
            Socket sock = serverSock.accept();
            System.out.println("socket accepted:" + sock);
            Thread thread = new Thread(() -> connectionEstablished(sock));
            thread.start();
        }
    }

    private void connectionEstablished(Socket socket) {
        try {

            byte[] buf = new byte[5];

            int n = socket.getInputStream().read(buf);
            System.out.println(new String(buf, 0, n));
            socket.getOutputStream().write("world".getBytes());
            socket.getOutputStream().flush();
            Thread.sleep(1000);
            InputStream in = socket.getInputStream();
            while (true) {
                int ch = in.read();
                if (ch == -1) {
                    System.out.println("EOF");
                    return;
                }
                System.out.print((char) ch);
            }
        } catch (Exception ex) {
            LOGGER.error("Error handling client", ex);
        }
    }

}
