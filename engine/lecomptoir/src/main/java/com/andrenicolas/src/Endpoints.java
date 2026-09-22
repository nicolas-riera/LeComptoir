package com.andrenicolas.src;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static com.andrenicolas.src.Tools.*;

import com.sun.net.httpserver.HttpServer;

public class Endpoints {
    private final List<Product> catalog;
    private final Cart cart;
    private final Checkout checkout;

    public Endpoints(List<Product> catalog, Cart cart, Checkout checkout) {
        this.catalog = catalog;
        this.cart = cart;
        this.checkout = checkout;
    }

    public void register(HttpServer server) {
        // 1. GET /api/products
        server.createContext("/api/products", exchange -> {
            addCorsHeaders(exchange);
            if (isOptions(exchange)) return;

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < catalog.size(); i++) {
                    Product p = catalog.get(i);
                   json.append(String.format(
                        Locale.US,
                        "{\"ref\":\"%s\",\"label\":\"%s\",\"price\":%.2f,\"category\":\"%s\"}",
                        p.getReference(),
                        p.getLabel(),
                        p.getUnitPrice(),
                        p.getCategory()
                    ));
                    if (i < catalog.size() - 1) json.append(",");
                }
                json.append("]");

                sendJsonResponse(exchange, 200, json.toString());
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        // 2. POST /api/cart/add
        server.createContext("/api/cart/add", exchange -> {
            addCorsHeaders(exchange);
            if (isOptions(exchange)) return;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                String ref = extractJsonValue(body, "ref");
                int quantity = Integer.parseInt(extractJsonValue(body, "quantity"));

                Product target = catalog.stream()
                        .filter(p -> p.getReference().equals(ref))
                        .findFirst()
                        .orElse(null);

                if (target != null) {
                    CartLine line = new CartLine(target, quantity);
                    cart.AddCartline(line);
                    sendJsonResponse(exchange, 200, "{\"message\":\"Product added\"}");
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        // 3. POST /api/checkout
        server.createContext("/api/checkout", exchange -> {
            addCorsHeaders(exchange);
            if (isOptions(exchange)) return;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String receipt = checkout.showReceipt(cart);
                String jsonResponse = String.format(
                    Locale.US,
                    "{\"receipt\":\"%s\",\"total\":%.2f}",
                    receipt.replace("\n", "\\n").replace("\"", "\\\""),
                    cart.getSubTotal()
                );

                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        // POST /api/cart/clear
        server.createContext("/api/cart/clear", exchange -> {
            addCorsHeaders(exchange);
            if (isOptions(exchange)) return;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                cart.clear();
                sendJsonResponse(exchange, 200, "{\"message\":\"Cart cleared\"}");
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });
    }
}
