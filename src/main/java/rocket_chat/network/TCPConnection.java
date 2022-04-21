package rocket_chat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final Thread thread;
    private final TCPConnectionListener eventListener;
    private BufferedReader in;
    private BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String ip, int port, String login) throws IOException {
        this.socket = new Socket(ip, port);
        this.eventListener = eventListener;
        initializer(socket);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnected(TCPConnection.this, login);
                    while (!thread.isInterrupted()) {
                        eventListener.onReceiveMessage(TCPConnection.this, in.readLine());
                    }
                } catch (
                        IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this, login);
                }
            }
        });
        thread.start();
    }
    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        initializer(socket);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line = null;
                try {
                    line = in.readLine();
                    eventListener.onConnected(TCPConnection.this, line);
                    while (!thread.isInterrupted()) {
                        eventListener.onReceiveMessage(TCPConnection.this, in.readLine());
                    }
                } catch (
                        IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this, line);
                }
            }
        });
        thread.start();
    }

    private void initializer(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public synchronized void sendMessage(String message) {
        try {
            out.write(message + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void sendLogin(String login) {
        try {
            out.write(login + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }
}
