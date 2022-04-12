package rocket_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rocket_chat.entity.User;

import java.io.IOException;

public class Main extends Application {
    public static User user;
    static private Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setResizable(false);
        stage.setTitle("RocketChat");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }

    public static void showChat(User user) throws IOException {
        Main.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    public static void showError(String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("error.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void showLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }
}