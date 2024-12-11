package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("[CLIENT] Підключено до сервера");

            // Потік для читання повідомлень від сервера
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("[CLIENT] З'єднання з сервером закрито.");
                }
            }).start();

            // Потік для відправки повідомлень на сервер
            while (true) {
                System.out.print("Введіть повідомлення (або 'exit' для виходу): ");
                String message = scanner.nextLine();
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[CLIENT] Помилка: " + e.getMessage());
        }
    }
}

