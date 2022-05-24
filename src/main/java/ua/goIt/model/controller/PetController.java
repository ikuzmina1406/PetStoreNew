package ua.goIt.model.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.Pet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.util.List;

public class PetController {
        private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
        private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public Pet getPetById(String url, String param) throws IOException, InterruptedException {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Pet.class);
    }


        public List<Pet> getPetsByStatus(String url, String param) throws IOException, InterruptedException {
            if (!param.equals("available") && !param.equals("pending") && !param.equals("sold")) {
                System.out.printf("You entered an invalid status %s, try again .",param);
            }
            String newUrl = url + "/findByStatus?status=" + param;
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(newUrl))
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return GSON.fromJson(send.body(), new TypeToken<List<Pet>>() {
            }.getType());
        }


        public ApiResponse deletePetById(String url, String param) throws IOException, InterruptedException {
            String newUrl = url + "/" + param;
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(newUrl))
                    .DELETE()
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return GSON.fromJson(send.body(),ApiResponse.class);

        }


        public Pet updatePet(String url, Pet pet) throws IOException, InterruptedException {
            String requestBody = GSON.toJson(pet);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return GSON.fromJson(send.body(), Pet.class);

        }


        public Pet createPet(String url, Pet pet) throws IOException, InterruptedException {
            String requestBody = GSON.toJson(pet);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .headers("Content-Type", "application/json")
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return GSON.fromJson(send.body(), Pet.class);

        }


        public ApiResponse updatePetsStatus(String url, Pet pet) throws IOException, InterruptedException {
            String newUrl = url + "/" + pet.getId();
            String requestBody = GSON.toJson(pet);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(newUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return GSON.fromJson(send.body(), ApiResponse.class);

        }
}
