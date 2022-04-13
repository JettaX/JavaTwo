package com.geekbrains.network;

public interface ConnectionListener {
    void onConnected(Connection connection);
    void onMessageReceived(Connection connection, String message);
    void onDisconnected(Connection connection);
    void onException(Connection connection, Exception e);
}
