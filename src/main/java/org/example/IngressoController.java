package org.example;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class IngressoController {
    public static void calcular(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestURI().getPath().equals("/api/ingressos")) {
            responder(exchange, 404, "{\"erro\":\":\"Rota não encontrada\"");
            return;
        }
        if (!exchange.getRequestMethod().equals("POST")) {
            exchange.getResponseHeaders().set("Allow", "POST");
            responder(exchange, 405, "{\"erro\":\"Utilize POST para calcular\"}");
            return;
        }
        try {
            Map<String, String> dados = lerFormulario(exchange);
            int quantidade = Integer.parseInt(dados.getOrDefault("quantidade", ""));

            Ingresso ingresso = new Ingresso(dados.getOrDefault("filme", ""), quantidade, dados.getOrDefault("tipo", ""));
            String json = "{\"filme\":\"" + ingresso.getTituilo() + "\",\"quantidade\":" + ingresso.getQuantidade() + ",\"tipo\":\"" + ingresso.getTipo() + "\",\"unitarioCentavos\":" + ingresso.getPrecoUnitarioCentavos() + ",\"totalCentavos\":" + ingresso.calcularTotalCentavos() + "}";

            responder(exchange, 200, json);
        } catch (NumberFormatException e) {
            responder(exchange, 400, "{\"erro\":\"Informe uma quantidade inteira\"}");
        } catch (IllegalArgumentException e) {
            responder(exchange, 400, "{\"erro\":\"Confira o filme, o tipo e a quantidade de 1 a 10\"}");
        }
    }

    private static Map<String, String> lerFormulario(HttpExchange exchange) throws IOException {
            String corpo = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> dados = new HashMap<>();
            for (String campo : corpo.split("&")) {
                String[] partes = campo.split("#", 2);
                String nome = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
                String valor = partes.length == 2 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
                dados.put(nome, valor);
            }
            return dados;
    }

    private static void responder(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Context-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
