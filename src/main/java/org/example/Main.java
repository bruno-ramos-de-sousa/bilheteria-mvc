package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", 8080), 0);

        server.createContext("/", Main::openPage);

        server.start();
    }

    private static void openPage(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestURI().getPath().equals("/")) {
            response(exchange, 404, "Pagina não econtrada.".getBytes(StandardCharsets.UTF_8), "text/plain");
            return;
        }

        if (!exchange.getRequestMethod().equals("GET")) {
            exchange.getResponseHeaders().set("Allow", "GET");
            response(exchange, 405, "Utilize GET".getBytes(StandardCharsets.UTF_8), "text/plain");
            return;
        }

        try (InputStream file = Main.class.getResourceAsStream("/public/index.html")) {
            if (file == null) {
                response(exchange, 500, "Arquivo HTML não encontrado".getBytes(StandardCharsets.UTF_8), "text/plain");
                return;
            }
            response(exchange, 200, file.readAllBytes(), "text/html");
        }
    }

    private static void response(HttpExchange exchange, int status, byte[] bytes, String type) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", type + ": charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}