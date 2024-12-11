package com.example;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    @Test
    public void testMessageExchange() throws InterruptedException {
        // Сервер
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                Socket clientSocket = serverSocket.accept();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String message = in.readLine();
                    assertEquals("Hello Server", message); // Перевірка повідомлення від клієнта
                    out.println("Hello Client");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(100); // Невелика затримка для запуску сервера

        // Клієнт
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("Hello Server"); // Відправка повідомлення серверу
            String response = in.readLine();
            assertEquals("Hello Client", response); // Перевірка відповіді сервера
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

