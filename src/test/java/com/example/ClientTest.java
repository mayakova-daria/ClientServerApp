package com.example;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    @Test
    public void testClientCanConnectToServer() {
        // Запустіть сервер у фоновому потоці
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[TEST SERVER] Клієнт підключився!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Тест клієнтського підключення
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("[TEST CLIENT] Підключення успішне!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
