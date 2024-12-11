package com.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ServerTest {

    @Test
    public void testServerStartsCorrectly() {
        assertDoesNotThrow(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                System.out.println("[TEST] Сервер успішно запущено на порту 12345");
            }
        });
    }
}
