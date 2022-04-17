package rocket_chat.repository;

import rocket_chat.network.TCPConnection;
import rocket_chat.network.TCPConnectionListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Connections {
    private static Map<String, TCPConnection> connections = new HashMap<>();

    public void addIfNotExists(String from, String to, TCPConnectionListener listener) throws IOException {
        if (!connections.containsKey(from.concat(":").concat(to))) {
            TCPConnection connection = new TCPConnection(listener, "localhost", 8188);
            connection.setFromTo(from.concat(":").concat(to));
            System.out.println(connection);
            connections.put(from.concat(":").concat(to), connection);
        }
    }

    public TCPConnection get(String from, String to) {
        return connections.get(from.concat(":").concat(to));
    }

    public void remove(String from, String to) {
        connections.remove(from.concat(":").concat(to));
    }
}
