package com.geekbrains.client;

import com.geekbrains.network.Connection;
import com.geekbrains.network.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements ConnectionListener {
    private final Logger logger = Logger.getLogger(Client.class.getName());
    private Connection connection;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        createConnection();
        sendMessage();
    }

    private void createConnection() {
        try {
            connection = new Connection("localhost", 8189, this);
        } catch (IOException e) {
            logger.log(Level.INFO, "Connection error");
        }
    }

    private void sendMessage() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            connection.sendMessage("Client: ".concat(scanner.nextLine()));
        }
    }

    @Override
    public void onConnected(Connection connection) {
        logger.log(Level.INFO, "Connected");
    }

    @Override
    public void onMessageReceived(Connection connection, String message) {
        printMassage(message);
    }

    @Override
    public void onDisconnected(Connection connection) {
        logger.log(Level.INFO, "Disconnected");
        connection.stop();
    }

    @Override
    public void onException(Connection connection, Exception e) {
        logger.log(Level.SEVERE, "Exception ".concat(e.getMessage()));
        connection.stop();
    }

    private void printMassage(String message) {
        System.out.println(message);
    }
}
