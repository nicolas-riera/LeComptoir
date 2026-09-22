package com.andrenicolas;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.Checkout;
import com.andrenicolas.src.Endpoints;
import com.andrenicolas.src.Product;
import com.andrenicolas.src.ProductCatalog;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class App 
{
    public static void main( String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        List<Product> catalog = ProductCatalog.getInitialProducts();
        Cart cart = new Cart();
        Checkout checkout = new Checkout();

        Endpoints endpoints = new Endpoints(catalog, cart, checkout);
        endpoints.register(server);

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://127.0.0.1:8080");
    }
}
