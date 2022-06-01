package ua.goIt.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserController {
    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public User getByUserName(String url, String param) throws IOException, InterruptedException {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), User.class);
    }

    public ApiResponse logsUser(String url, String param) throws IOException, InterruptedException {
        String[] words = param.split(" ");
        String newUrl = url + "/login?username=" + words[0] + "&password=" + words[1];
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }

    public ApiResponse getLogOut(String url) throws IOException, InterruptedException {
        String newUrl = url + "/logout";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }

    public ApiResponse createUserWithList(String url, List<User> user) throws IOException, InterruptedException {
        String newUrl = url + "/createWithList";
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }

    public ApiResponse createUser(String url, User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }

    public ApiResponse updateUser(String url, User user) throws IOException, InterruptedException {
        String newUrl = url + "/" + user.getUsername();
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return GSON.fromJson(send.body(), ApiResponse.class);

    }

    public ApiResponse deleteUser(String url, String param) throws IOException, InterruptedException {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .DELETE()
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);

    }
}
