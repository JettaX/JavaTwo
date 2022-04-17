package rocket_chat.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rocket_chat.entity.Message;
import rocket_chat.network.TCPConnection;
import rocket_chat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }
    private final Logger logger = Logger.getLogger(ChatServer.class.getName());
    private Map<String, TCPConnection> connections;

    private ChatServer() {
        logger.log(java.util.logging.Level.INFO, "Starting server...");
        connections = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(8188)) {
            while (true) {
                try {
                    new TCPConnection(serverSocket.accept(), this);
                } catch (IOException e) {
                    logger.log(java.util.logging.Level.SEVERE, "Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnected(TCPConnection tcpConnection, String fromTo) {
        connections.put(fromTo, tcpConnection);
        logger.log(java.util.logging.Level.INFO, "Client connected");
    }

    @Override
    public synchronized void onReceiveMessage(TCPConnection tcpConnection, String message) {
        sendMessage(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection, String fromTo) {
        connections.remove(fromTo);
        logger.log(java.util.logging.Level.INFO, "Client disconnected");
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        logger.log(java.util.logging.Level.SEVERE, "Error: " + e.getMessage());
    }

    private void sendMessage(String message) {
        Message mess = null;
        try {
            mess = new ObjectMapper().readerFor(Message.class).readValue(message);
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Error while parsing message", e);
        }

        for (Map.Entry<String, TCPConnection> connection : connections.entrySet()) {
            if (connection.getKey().equals(mess.getUserNameTo().concat(":").concat(mess.getUserNameFrom()))) {
                connection.getValue().sendMessage(message);
            }
        }
    }
}
