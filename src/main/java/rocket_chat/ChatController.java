package rocket_chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rocket_chat.entity.Chat;
import rocket_chat.entity.Message;
import rocket_chat.network.TCPConnection;
import rocket_chat.network.TCPConnectionListener;
import rocket_chat.repository.ChatRepository;
import rocket_chat.repository.ChatRepositoryInMemory;
import rocket_chat.repository.Connections;
import rocket_chat.validation.Validator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController implements TCPConnectionListener {
    private ChatRepository chatRepository;
    private Validator validator;
    private Chat chat;
    private Logger logger = Logger.getLogger(ChatController.class.getName());
    private TCPConnection connection;
    private Connections connections;

    @FXML
    public ScrollPane scrollPaneForMessages;
    @FXML
    Button sendButton;
    @FXML
    TextField inputField;
    @FXML
    public VBox messagesWrapper;
    @FXML
    public HBox titleWrapper;

    public void initialize() {
        chatRepository = new ChatRepositoryInMemory();
        validator = new Validator();
        connections = new Connections();
    }

    public void initializer(Chat chat) {
        this.chat = chat;
        try {
            connections.addIfNotExists(chat.getOwnerUser().getUserLogin(), chat.getFriendUser().getUserLogin(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection = connections.get(chat.getOwnerUser().getUserLogin(), chat.getFriendUser().getUserLogin());
        addMessages();
        generateTitle();
    }

    public void generateTitle() {
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            try {
                Main.showChats(Main.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        backButton.getStyleClass().add("backButton");
        titleWrapper.getChildren().add(backButton);

        Label title = new Label(chat.getFriendUser().getName() + " " + chat.getFriendUser().getSurname());
        title.getStyleClass().add("titleLabel");
        titleWrapper.setAlignment(Pos.CENTER_LEFT);
        titleWrapper.getChildren().add(title);
    }

    @FXML
    protected void sendMessageListener() {
        if (!validator.isValid(inputField.getText())) {
            inputField.clear();
            return;
        }

        addMessage(new Message(chat.getOwnerUser().getUserLogin(), chat.getFriendUser().getUserLogin(),
                inputField.getText()));
        inputField.clear();
        inputField.requestFocus();
    }

    @FXML
    protected void keyListener(KeyEvent event) {
        if (event.getCode().getCode() == 10) {
            sendMessageListener();
        }
    }

    public void addMessage(Message message) {
        if (chat.getMessages().isEmpty()) {
            chatRepository.saveChat(chat);
        }
        chatRepository.addMessage(message);
        try {
            connection.sendMessage(new ObjectMapper()
                    .writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Cant parse message ", e);
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Connection not found ", e);
        }

        HBox messageWrapper = new HBox();
        messageWrapper.getStyleClass().add("messageWrapper");

        Label labelMessage =
                new Label(message.getText() + "     " + message.getTime().getHour() + ":" + message.getTime().getMinute());
        labelMessage.getStyleClass().add("messageLabelOwner");
        labelMessage.setWrapText(true);

        messageWrapper.setAlignment(Pos.CENTER_RIGHT);
        messageWrapper.getChildren().add(labelMessage);
        messagesWrapper.getChildren().add(messageWrapper);

        scrollPaneForMessages.setVvalue(scrollPaneForMessages.getVmax());
    }

    public void addMessages() {
        for (Message message : chat.getMessages()) {
            HBox messageWrapper = new HBox();
            messageWrapper.getStyleClass().add("messageWrapper");
            Label label = new Label(message.getText() + "     " + message.getTime().getHour() + ":" + message.getTime().getMinute());

            label.setWrapText(true);
            if (message.getUserNameFrom().equals(chat.getOwnerUser().getUserLogin())) {
                label.getStyleClass().add("messageLabelOwner");
                messageWrapper.setAlignment(Pos.CENTER_RIGHT);
            } else {
                label.getStyleClass().add("messageLabelFriend");
                messageWrapper.setAlignment(Pos.CENTER_LEFT);
            }
            messageWrapper.getChildren().add(label);
            messagesWrapper.getChildren().add(messageWrapper);
        }
        scrollPaneForMessages.setVvalue(scrollPaneForMessages.getVmax());
    }

    public void mouseListener(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.BACK)) {
            try {
                Main.showChats(Main.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(TCPConnection tcpConnection, String fromTo) {

    }

    @Override
    public void onReceiveMessage(TCPConnection tcpConnection, String message) {
        Message mess = null;
        try {
            mess = new ObjectMapper().readerFor(Message.class).readValue(message);
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, "Error while parsing message", e);
        }
        chat.addMessage(mess);
        addMessage(mess);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection, String fromTo) {

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}