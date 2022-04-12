package rocket_chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import rocket_chat.validation.Validator;

public class ChatController {
    private Validator validator;
    @FXML
    TextArea chatWindow;
    @FXML
    Button sendButton;
    @FXML
    TextField inputField;

    public void initialize() {
        validator = new Validator();
    }

    @FXML
    protected void sendMessageListener() {
        if (!validator.isValid(inputField.getText())) {
            inputField.clear();
            return;
        }
        chatWindow.appendText(Main.user.getUsername() + ": " + inputField.getText() + "\n");
        inputField.clear();
        inputField.requestFocus();
    }
    @FXML

    protected void keyListener(KeyEvent event) {
        if (event.getCode().getCode() == 10) {
            sendMessageListener();
        }
    }
}