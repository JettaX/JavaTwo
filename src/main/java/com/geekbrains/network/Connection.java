package com.geekbrains.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    private ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;
    private boolean isConnected;

    public Connection(String ip, int port, ConnectionListener listener) throws IOException {
        this(new Socket(ip, port), listener);
    }

    public Connection(Socket socket, ConnectionListener listener) throws IOException {
        this.listener = listener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        new Thread(() -> {
            try {
                isConnected = true;
                listener.onConnected(Connection.this);
                while (isConnected) {
                    listener.onMessageReceived(Connection.this, in.readLine());
                }
                listener.onDisconnected(Connection.this);
            } catch (IOException e) {
                listener.onException(Connection.this, e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            isConnected = false;
            listener.onDisconnected(Connection.this);
        }).start();
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            listener.onException(this, e);
        }
    }

    public void stop() {
        isConnected = false;
    }
}
