package ua.goIt.commands.entitiesCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.commands.Commands;
import ua.goIt.controller.OrderController;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.Order;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class OrderCommands implements Commands {
    private final Order order = new Order();
    private final OrderController orderController = new OrderController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/store";
    private final List<String> commands = Arrays.asList("\"getInventory\"", "\"getOrderByPetsId\"", "\"createOrder\"",
            "\"deleteOrderById\"");

    @Override
    public void handle(String param, Consumer<Commands> consumer) throws IOException, InterruptedException {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getinventory":
                getInventory(newParam);
                return;
            case "getorderbypetsid":
                getOrderByPetsId(newParam);
                return;
            case "createorder":
                createOrder(newParam);
                return;
            case "deleteorderbyid":
                deleteOrderById(newParam);
        }
    }

    private void deleteOrderById(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        ApiResponse apiResponse = orderController.deleteOrderById(URL_NAME, String.valueOf(Integer.parseInt(param)));
        if (apiResponse.getCode() != 200) {
            System.out.printf("Order with id %s is not found. Enter another id , please .", param);
            return;
        }

        System.out.printf("Order with  id %s was deleted.\n", param);
        System.out.println(GSON.toJson(apiResponse));

    }


    private void createOrder(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\d+)\\s{1}(\\d+)\\s{1}(\\d{4}-\\d{2}-\\d{2})\\s{1}(\\w+)\\s{1}(\\w+)$")) {
            System.out.println("Your input could not be processed, try again");
            return;
        }
        String[] words = param.split(" ");
        order.setId(Long.valueOf(words[0]));
        order.setPetId(Long.parseLong(words[1]));
        order.setQuantity(Long.parseLong(words[2]));
        order.setShipDate(words[3]);
        order.setStatus(words[4]);
        order.setComplete(Boolean.parseBoolean(words[5]));
        Order createdOrder = orderController.placeAnOrderForAPet(URL_NAME, order);
        System.out.println(GSON.toJson(createdOrder));

    }

    private void getOrderByPetsId(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        Order orderByPetsId = orderController.getOrderByPetsId(URL_NAME, String.valueOf(Integer.parseInt(firstWord.get())));
        if (orderByPetsId.getId() == null) {
            System.out.printf("Order with id %s is not found. Enter another id , please .", firstWord.get());
            return;
        }
        System.out.println("Your order.");
        System.out.println(GSON.toJson(orderByPetsId));
    }


    private void getInventory(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        String storeInventory = orderController.getStoreInventory(URL_NAME, firstWord.get());
        System.out.println(GSON.toJson(storeInventory));

    }


    @Override
    public void printInstruction() {
        System.out.println("Make your choice please - " + commands);
        System.out.println("If you will choice \"getInventory\"  enter with a space: getInventory inventory. For example: getInventory inventory");
        System.out.println("If you will choice \"getOrderByPetsId\" enter with a space: getOrderByPetsId (id - pet). For example: getOrderByPetsId 1");
        System.out.println("If you will choice  \"createOrder\"enter with a space: createOrder id (- order) id (- pet)" +
                " quantity shipDate status complete.\n For example: createOrder 1 1 3 2021-11-16 placed true. " +
                "The orders has three types of : {approved, placed, delivered}.");
        System.out.println("If you will choice  \"deleteOrderById\" enter with a space: " +
                "deleteOrderById (id - you want to delete). For example: deleteOrderById 1");
    }
}
