module com.example.chatonjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens rocket_chat to javafx.fxml;
    exports rocket_chat;
}