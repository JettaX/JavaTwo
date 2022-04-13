package com.geekbrains.server;

import com.geekbrains.network.Connection;
import com.geekbrains.network.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements ConnectionListener {
    private final Logger logger = Logger.getLogger(Server.class.getName());
    private Connection connection;
    private boolean isConnected = false;


    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        connection = waitConnection();
        Scanner scanner = new Scanner(System.in);
        while (isConnected && connection != null) {
            connection.sendMessage("Server: ".concat(scanner.nextLine()));
        }
    }

    @Override
    public void onConnected(Connection connection) {
        logger.log(Level.INFO, "Connected");
        isConnected = true;
    }

    @Override
    public void onMessageReceived(Connection connection, String message) {
        printMassage(message);
    }

    @Override
    public void onDisconnected(Connection connection) {
        logger.log(Level.INFO, "Disconnected");
        isConnected = false;
        connection.stop();
        start();
    }

    @Override
    public void onException(Connection connection, Exception e) {
        if (!isConnected) {
            logger.log(Level.INFO, "Connection lost");
            start();
        } else {
            logger.log(Level.INFO, "Exception ".concat(e.getMessage()));
        }
    }

    private void printMassage(String message) {
        System.out.println(message);
    }

    private Connection waitConnection() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            logger.log(Level.INFO, "Server started");
            return new Connection(serverSocket.accept(), this);
        } catch (IOException e) {
            logger.log(Level.INFO, "Exception ".concat(e.getMessage()));
        }
        return null;
    }
}