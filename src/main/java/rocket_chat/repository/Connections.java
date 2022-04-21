package rocket_chat.repository;

import rocket_chat.Main;
import rocket_chat.network.TCPConnection;
import rocket_chat.network.TCPConnectionListener;

import java.io.IOException;

public class Connections {
    private static TCPConnection connection = null;

    public void addIfNotExists(TCPConnectionListener listener) throws IOException {
        if (connection == null) {
            TCPConnection con = new TCPConnection(listener, "localhost", 8188, Main.user.getUserLogin());
            con.sendLogin(Main.user.getUserLogin());
            connection = con;
            Main.isServerConnected = true;
        }
    }

    public TCPConnection get() {
        return connection;
    }

    public void remove() {
        connection = null;
    }
}
