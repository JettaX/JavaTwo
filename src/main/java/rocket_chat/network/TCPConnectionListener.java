package rocket_chat.network;

public interface TCPConnectionListener {
    void onConnected(TCPConnection tcpConnection, String fromTo);

    void onReceiveMessage(TCPConnection tcpConnection, String message);

    void onDisconnect(TCPConnection tcpConnection, String fromTo);

    void onException(TCPConnection tcpConnection, Exception e);
}
