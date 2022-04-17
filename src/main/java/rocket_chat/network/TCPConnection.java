package rocket_chat.network;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {

    private final Socket socket;
    private final Thread thread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String ip, int port) throws IOException {
        this(new Socket(ip, port), eventListener);
    }

    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fromTo = "";
                try {
                    do {
                        fromTo = in.readLine();
                        if (!fromTo.isBlank()) {
                            break;
                        }
                    } while (true);
                    eventListener.onConnected(TCPConnection.this, fromTo);
                    while (!thread.isInterrupted()) {
                        eventListener.onReceiveMessage(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this, fromTo);
                }
            }
        });
        thread.start();
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

    public synchronized void setFromTo(String fromTo) {
        try {
            out.write(fromTo + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    @SneakyThrows
    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }
}
