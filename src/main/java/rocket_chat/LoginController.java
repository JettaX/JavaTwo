package rocket_chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import rocket_chat.entity.User;
import rocket_chat.validation.Validator;

import java.io.IOException;

public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    public TextField inputLogin;

    public void loginButtonAction() {
        String login = inputLogin.getText();
        if (new Validator().isValid(login)) {
            try {
                Main.showChat(new User(login));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Main.showError("Неверный логин");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
