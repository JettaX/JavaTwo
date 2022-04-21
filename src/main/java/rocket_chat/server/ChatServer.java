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
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final Logger logger = Logger.getLogger(ChatServer.class.getName());
    private Map<String, TCPConnection> connections;
    private Map<String, Queue<String>> queues = new HashMap<>();

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
    public synchronized void onConnected(TCPConnection tcpConnection, String login) {
        connections.put(login, tcpConnection);
        checkQueues(login);
        logger.log(java.util.logging.Level.INFO, "Client connected");
    }

    @Override
    public synchronized void onReceiveMessage(TCPConnection tcpConnection, String message) {
        sendMessage(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection, String login) {
        connections.remove(login);
        logger.log(java.util.logging.Level.INFO, "Client disconnected");
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        logger.log(java.util.logging.Level.SEVERE, "Error:");
    }

    private void sendMessage(String message) {
        Message mess = parseMessage(message);

        String connectionId = mess.getUserNameTo();

        if (connections.containsKey(connectionId)) {
            connections.get(connectionId).sendMessage(message);
        } else {
            addMessageInQueue(message, mess);
        }
    }

    private void addMessageInQueue(String gsonMessage, Message message) {
        String connectionId = message.getUserNameTo();
        if (queues.containsKey(connectionId)) {
            Queue<String> queue = queues.get(connectionId);
            queue.add(gsonMessage);
        } else {
            Queue<String> queue = new java.util.LinkedList<>();
            queue.add(gsonMessage);
            queues.put(connectionId, queue);
        }
    }

    private void checkQueues(String login) {
        if (queues.containsKey(login)) {
            Queue<String> queue = queues.get(login);
            while (!queue.isEmpty()) {
                connections.get(login).sendMessage(queue.poll());
            }
        }
    }

    private Message parseMessage(String message) {
        Message mess = null;
        try {
            mess = new ObjectMapper().readerFor(Message.class).readValue(message);
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Error while parsing message", e);
        }
        return mess;
    }
}
