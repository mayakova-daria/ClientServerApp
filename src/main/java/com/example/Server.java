package com.example;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private static final List<ClientHandler> activeConnections = new ArrayList<>();
    private static int clientCounter = 1;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Сервер запущено на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientName = "client-" + clientCounter++;
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                activeConnections.add(clientHandler);
                threadPool.execute(clientHandler);

                System.out.println("[SERVER] " + clientName + " успішно підключився");
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Помилка: " + e.getMessage());
        }
    }

    // Клас для обробки клієнтів
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final String clientName;
        private final String connectionTime;

        public ClientHandler(Socket clientSocket, String clientName) {
            this.clientSocket = clientSocket;
            this.clientName = clientName;
            this.connectionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                out.println("[SERVER] Вітаємо, " + clientName + "! Ви підключені.");
                System.out.println("[SERVER] Клієнт " + clientName + " підключився о " + connectionTime);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        disconnectClient();
                        break;
                    }
                    out.println("[SERVER] Ви відправили: " + message);
                }
            } catch (IOException e) {
                System.err.println("[SERVER] Помилка з клієнтом " + clientName + ": " + e.getMessage());
            }
        }

        private void disconnectClient() throws IOException {
            activeConnections.remove(this);
            clientSocket.close();
            System.out.println("[SERVER] " + clientName + " відключився.");
        }
    }
}

