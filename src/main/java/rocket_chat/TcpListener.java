package rocket_chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import rocket_chat.entity.Message;
import rocket_chat.network.TCPConnection;
import rocket_chat.network.TCPConnectionListener;
import rocket_chat.repository.ChatRepository;
import rocket_chat.repository.ChatRepositoryInMemory;
import rocket_chat.repository.Connections;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpListener implements TCPConnectionListener {
    private Logger logger = Logger.getLogger(TcpListener.class.getName());
    private Connections connections;
    private ChatRepository chatRepository;
    private Main main;

    public TcpListener(Main main) {
        this.main = main;
        connections = new Connections();
        chatRepository = new ChatRepositoryInMemory();
        createConnections();
    }

    @Override
    public void onConnected(TCPConnection tcpConnection, String login) {
        Main.isFriendConnected = true;
    }

    @Override
    public void onReceiveMessage(TCPConnection tcpConnection, String message) {
        Message mess = null;
        try {
            mess = new ObjectMapper().readerFor(Message.class).readValue(message);
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Error while parsing message", e);
        }
        if (main.chatController != null && main.chatController.getChat().getFriendUser().getUserLogin().equals(Objects.requireNonNull(mess).getUserNameFrom())) {
            Message finalMess = mess;
            Platform.runLater(() -> main.chatController.addMessage(finalMess, false));
        } else {
            chatRepository.addMessage(mess);
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection, String login) {
        tcpConnection.disconnect();
        connections.remove();
        Main.isFriendConnected = false;
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        logger.log(Level.WARNING, "Exception");
        tcpConnection.disconnect();
        connections.remove();
        Main.isFriendConnected = false;
        Main.isServerConnected = false;
    }

    private void createConnections() {
        try {
            connections.addIfNotExists(this);
        } catch (ConnectException e) {
            Main.isServerConnected = false;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error");
        }
    }
}
