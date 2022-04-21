package rocket_chat.network;

public interface TCPConnectionListener {
    void onConnected(TCPConnection tcpConnection, String login);
    void onReceiveMessage(TCPConnection tcpConnection, String message);
    void onDisconnect(TCPConnection tcpConnection, String login);
    void onException(TCPConnection tcpConnection, Exception e);
}
