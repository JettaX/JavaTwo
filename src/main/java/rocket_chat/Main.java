package rocket_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rocket_chat.entity.Chat;
import rocket_chat.entity.Message;
import rocket_chat.entity.User;
import rocket_chat.entity.UserSecure;
import rocket_chat.repository.*;

import java.io.IOException;

public class Main extends Application {
    //TODO Не до конца происходит прокрутка сообщений при отправки или получении сообщений
    public static User user;
    private Stage stage;
    public ChatController chatController;
    public static boolean isFriendConnected = false;
    public static boolean isServerConnected = false;
    private Thread thread;

    @Override
    public void start(Stage stage) throws IOException {
        initializerData();
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        LoginController loginController = fxmlLoader.getController();
        loginController.setMain(this);
        stage.setResizable(true);
        stage.setTitle("RocketChat");
        stage.setScene(scene);
        stage.show();
        thread = connectionCheckerThread();
        thread.start();
    }

    public static void main(String[] args) {
        launch();
    }

    public void showChat(Chat chat) throws IOException {
        stage.close();
        FXMLLoader fxmlLoader = createStage("chat.fxml");
        ChatController chatController = fxmlLoader.getController();
        this.chatController = chatController;
        chatController.initializer(chat, this);
    }

    public void showChats(User user) throws IOException {
        this.user = user;
        stage.close();
        FXMLLoader fxmlLoader = createStage("chats.fxml");
        ChatsButtonsController chatsButtonsController = fxmlLoader.getController();
        chatsButtonsController.initializer(this);
        nullingLink();
    }

    public void showError(String message) throws IOException {
        FXMLLoader fxmlLoader = createStage("error.fxml");
        ErrorController errorController = fxmlLoader.getController();
        errorController.setErrorMessage(message);
        errorController.setMain(this);
        nullingLink();
    }

    public void showLogin() throws IOException {
        stage.close();
        FXMLLoader fxmlLoader = createStage("login.fxml");
        LoginController loginController = fxmlLoader.getController();
        loginController.setMain(this);
        nullingLink();
    }

    private FXMLLoader createStage(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), stage.getWidth() - 16, stage.getHeight() - 39);
        double x = stage.getX();
        double y = stage.getY();
        stage.setScene(scene);
        stage.setX(x);
        stage.setY(y);
        stage.show();
        return fxmlLoader;
    }

    private void nullingLink() {
        chatController = null;
    }

    private void createConnection() throws InterruptedException {
        if (!isServerConnected && user != null) {
            new TcpListener(this, user);
        } else {
            Thread.sleep(5000);
        }
    }

    private Thread connectionCheckerThread() {
        return new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    createConnection();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void initializerData() {
        ChatRepository chatRepository = new ChatRepositoryInMemory();
        UserRepository userRepository = new UserRepositoryInMemory();
        UserSecureRepository userSecureRepository = new UserSecureRepositoryInMemory();

        User mainUser = new User("admin", "Max", "Maxon");
        User userOne = new User("lilyPit", "Lily", "Pitersky");
        User userTwo = new User("Karmenchik", "Karen", "Mamonage");
        User userThree = new User("SteveApple", "Steve", "Jobs");
        User userFour = new User("Jonson@Lol", "Jon", "Ar");
        User userFive = new User("KittyClair", "Karen", "Clair");
        User userSix = new User("KekLol", "Sara", "Bond");

        userOne.setImagePath("/images/iconsForUsers/1.jpg");
        userTwo.setImagePath("/images/iconsForUsers/2.jpg");
        userThree.setImagePath("/images/iconsForUsers/3.jpg");

        UserSecure userOneSecure = new UserSecure(userOne.getUserLogin(), "1234");
        UserSecure userTwoSecure = new UserSecure(userTwo.getUserLogin(), "1234");
        UserSecure userThreeSecure = new UserSecure(userThree.getUserLogin(), "1234");
        UserSecure mainUserSecure = new UserSecure(mainUser.getUserLogin(), "1234");
        UserSecure userFourSecure = new UserSecure(userFour.getUserLogin(), "1234");
        UserSecure userFiveSecure = new UserSecure(userFive.getUserLogin(), "1234");
        UserSecure userSixSecure = new UserSecure(userSix.getUserLogin(), "1234");

        userSecureRepository.createUserSecure(userOneSecure);
        userSecureRepository.createUserSecure(userTwoSecure);
        userSecureRepository.createUserSecure(userThreeSecure);
        userSecureRepository.createUserSecure(userFourSecure);
        userSecureRepository.createUserSecure(userFiveSecure);
        userSecureRepository.createUserSecure(userSixSecure);
        userSecureRepository.createUserSecure(mainUserSecure);

        Chat chatWithOne = new Chat(mainUser, userOne);
        Chat chatWithTwo = new Chat(mainUser, userTwo);

        Chat chatWithThree = new Chat(userOne, mainUser);
        Chat chatWithFour = new Chat(userOne, userThree);

        Message message1 = new Message(userOne.getUserLogin(), mainUser.getUserLogin(), "Hi");
        Message message2 = new Message(mainUser.getUserLogin(), userOne.getUserLogin(), "Lol");
        Message message3 = new Message(userOne.getUserLogin(), mainUser.getUserLogin(), "Bye");
        Message message4 = new Message(mainUser.getUserLogin(), userOne.getUserLogin(), "Kek");
        Message message5 = new Message(userOne.getUserLogin(), mainUser.getUserLogin(), "Chill");
        Message message6 = new Message(userTwo.getUserLogin(), mainUser.getUserLogin(), "Sleep");
        Message message7 = new Message(mainUser.getUserLogin(), userTwo.getUserLogin(), "Go");
        Message message8 = new Message(userTwo.getUserLogin(), mainUser.getUserLogin(), "Work");
        Message message9 = new Message(mainUser.getUserLogin(), userTwo.getUserLogin(), "Dance");
        Message message11 = new Message(mainUser.getUserLogin(), userTwo.getUserLogin(), "Конструктор поля JFormattedTextField в " +
                "качестве параметра" +
                " " +
                "получает форматирующий объект, унаследованный от абстрактного внутреннего класса AbstractFormatter. Когда в форматированное текстовое поле вводятся символы, то сразу же вызывается форматирующий объект, в задачу которого входит анализ введенного значения и принятие решения о соответствии этого значения некоторому формату. Основными составляющими форматирующего объекта являются фильтр документа DocumentFilter, который принимает решение, разрешать или нет очередное изменение в документе, а также навигационный фильтр NavigationFilter. Навигационный фильтр получает исчерпывающую информацию о перемещениях курсора в текстовом поле и способен запрещать курсору появляться в некоторых областях поля (таких как разделители номеров, дат и других данных, которые не должны редактироваться). Форматирующий объект также отвеачет за действие, которое предпринимается в случае ввода пользователем неверного значения (по умолчанию раздается звуковой сигнал).");
        Message message10 = new Message(userTwo.getUserLogin(), mainUser.getUserLogin(), "Out");

        chatWithOne.addMessage(message1);
        chatWithOne.addMessage(message2);
        chatWithOne.addMessage(message3);
        chatWithOne.addMessage(message4);
        chatWithOne.addMessage(message5);


        chatWithTwo.addMessage(message6);
        chatWithTwo.addMessage(message7);
        chatWithTwo.addMessage(message8);
        chatWithTwo.addMessage(message9);
        chatWithTwo.addMessage(message10);
        chatWithTwo.addMessage(message11);

        userRepository.saveUser(mainUser);
        userRepository.saveUser(userOne);
        userRepository.saveUser(userTwo);
        userRepository.saveUser(userThree);
        userRepository.saveUser(userFour);
        userRepository.saveUser(userFive);
        userRepository.saveUser(userSix);

        chatRepository.saveChat(chatWithOne);
        chatRepository.saveChat(chatWithTwo);
        chatRepository.saveChat(chatWithThree);
        chatRepository.saveChat(chatWithFour);
    }
}