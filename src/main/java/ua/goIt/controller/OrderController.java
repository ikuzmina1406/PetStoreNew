package ua.goIt.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.Order;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OrderController {
    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String getStoreInventory(String url,String param) throws IOException, InterruptedException {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return send.body();
    }
    public Order getOrderByPetsId(String url, String param) throws IOException, InterruptedException {
        String newUrl = url + "/order/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Order.class);
    }
    public Order placeAnOrderForAPet(String url, Order order) throws IOException, InterruptedException {
        String newUrl = url + "/order";
        String requestBody = GSON.toJson(order);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Order.class);
    }
    public ApiResponse deleteOrderById(String url, String param) throws IOException, InterruptedException {
        String newUrl = url + "/order/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .DELETE()
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(),ApiResponse.class);

    }
}
