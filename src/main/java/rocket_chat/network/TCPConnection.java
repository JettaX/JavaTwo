package rocket_chat.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rocket_chat.entity.Message;

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
                int counter = 0;
                String fromTo = "";
                String message = "";
                try {
                    fromTo = in.readLine();
                    if (fromTo.startsWith("{")) {
                        message = fromTo;
                        try {
                            Message messageObj = new ObjectMapper().readerFor(Message.class).readValue(message);
                            fromTo = messageObj.getUserNameFrom().concat(":").concat(messageObj.getUserNameTo());
                        } catch (JsonProcessingException e) {
                            eventListener.onException(TCPConnection.this, e);
                        }
                    }
                    eventListener.onConnected(TCPConnection.this, fromTo);
                    while (!thread.isInterrupted()) {
                        String line = "";
                        if (counter == 0 && !message.isBlank()) {
                            line = message;
                            counter++;
                        } else {
                            line = in.readLine();
                        }
                        eventListener.onReceiveMessage(TCPConnection.this, line);
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

    public synchronized void disconnect() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }
}
