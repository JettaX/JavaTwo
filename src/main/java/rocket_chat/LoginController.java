package rocket_chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import rocket_chat.repository.UserRepository;
import rocket_chat.repository.UserRepositoryInMemory;
import rocket_chat.repository.UserSecureRepository;
import rocket_chat.repository.UserSecureRepositoryInMemory;
import rocket_chat.validation.Validator;

import java.io.IOException;

public class LoginController {
    @Setter
    private Main main;
    private UserRepository userRepository;
    private UserSecureRepository userSecureRepository;
    @FXML
    public Button loginButton;
    @FXML
    public TextField inputLogin;
    @FXML
    public TextField inputPassword;

    public void initialize() {
        userSecureRepository = new UserSecureRepositoryInMemory();
        userRepository = new UserRepositoryInMemory();
    }

    public void loginButtonAction() {
        String login = inputLogin.getText();
        String password = inputPassword.getText();
        if (new Validator().isValid(login) && userSecureRepository.checkAuth(login, password)) {
            try {
                main.showChats(userRepository.getUserByUserLogin(login));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                main.showError("username or password is incorrect");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
