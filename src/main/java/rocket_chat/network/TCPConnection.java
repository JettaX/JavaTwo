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

    public TCPConnection(TCPConnectionListener eventListener, String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.eventListener = eventListener;
        initializer(socket);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String login = "";
                try {
                    login = in.readLine();
                    eventListener.onAuthSuccess(TCPConnection.this, login);
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
                String login = null;
                int countError = 0;
                try {
                    while (!thread.isInterrupted()) {
                        try {
                            login = in.readLine();
                            eventListener.onAttemptAuth(TCPConnection.this, login);
                        } catch (IOException e) {
                            if (countError++ > 1) {
                                disconnect();
                            }
                            eventListener.onAuthFailed(TCPConnection.this, e);
                            continue;
                        }
                        login = login.split(":")[0];
                        eventListener.onAuthSuccess(TCPConnection.this, login);
                        eventListener.onConnected(TCPConnection.this, login);
                        break;
                    }
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

    private void initializer(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }
    public synchronized void sendMessage(String message) {
        write(message);
    }

    public synchronized void sendLogin(String login, String password) {
        write(login.concat(":").concat(password));
    }

    public synchronized void authSuccess(String login) {
        write(login);
    }

    private synchronized void write(String message) {
        try {
            out.write(message + "\r\n");
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
