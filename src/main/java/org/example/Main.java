package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", 8080), 0);

        server.createContext("/", Main::abrirPagina);

        server.start();
    }

    private static void abrirPagina(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestURI().getPath().equals("/")) {
            responderPagina(exchange, 404, "Pagina não econtrada.".getBytes(StandardCharsets.UTF_8), "text/plain");
            return;
        }
    }
}