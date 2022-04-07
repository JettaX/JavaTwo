package rocket_chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class Controller {
    @FXML
    TextArea chatWindow;
    @FXML
    Button sendButton;
    @FXML
    TextField inputField;

    @FXML
    protected void sendMessageListener() {
        chatWindow.appendText(inputField.getText() + "\n");
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