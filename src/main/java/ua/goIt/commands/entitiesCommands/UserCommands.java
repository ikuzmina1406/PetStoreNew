package ua.goIt.commands.entitiesCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.commands.Commands;
import ua.goIt.controller.UserController;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.User;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class UserCommands implements Commands {
    private final User user = new User();
    private final UserController userController = new UserController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/user";
    private final List<String> commands = Arrays.asList("\"getByUserName\"", "\"logUser\"", "\"logOut\"",
            "\"createWithList\"", "\"createUser\"", "\"updateUser\"", "\"deleteUser\"");

    @Override
    public void handle(String param, Consumer<Commands> consumer) throws IOException, InterruptedException {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getbyusername":
                getByUserName(newParam);
                return;
            case "loguser":
                logUser(newParam);
                return;
            case "logout":
                logOut();
                return;
            case "createwithlist":
                createUserWithList(newParam);
                return;
            case "createuser":
                createUser(newParam);
                return;
            case "updateuser":
                updateUser(newParam);
                return;
            case "deleteuser":
                deleteUser(newParam);


        }
    }

    private void deleteUser(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        ApiResponse apiResponse = userController.deleteUser(URL_NAME, param);
        if (apiResponse == null) {
            System.out.println("User with userName is not found.  Enter another userName , please .");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }

    private void updateUser(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        user.setUsername(firstWord.get());
        ApiResponse apiResponse = userController.updateUser(URL_NAME, user);
        if (apiResponse.getCode() != 200) {
            System.out.println("User with userName is not found.  Enter another userName , please .");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }

    private void createUser(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\d+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        String[] words = param.split(" ");
        user.setId(Long.valueOf(words[0]));
        user.setUsername(words[1]);
        user.setFirstName(words[2]);
        user.setLastName(words[3]);
        user.setEmail(words[4]);
        user.setPassword(words[5]);
        user.setPhone(words[6]);
        user.setUserStatus(Integer.parseInt(words[7]));
        ApiResponse withList = userController.createUser(URL_NAME, user);
        if (withList.getCode() != 200) {
            System.out.println("User already exists under this data. Enter other data .");
            return;
        }
        System.out.println("User created successfully .");
        System.out.println(GSON.toJson(withList));
    }

    private void createUserWithList(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\d+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        List<User> users = new ArrayList<>();
        String[] words = param.split(" ");
        user.setId(Long.valueOf(words[0]));
        user.setUsername(words[1]);
        user.setFirstName(words[2]);
        user.setLastName(words[3]);
        user.setEmail(words[4]);
        user.setPassword(words[5]);
        user.setPhone(words[6]);
        user.setUserStatus(Integer.parseInt(words[7]));
        users.add(user);
        ApiResponse withList = userController.createUserWithList(URL_NAME, users);
        if (withList.getCode() != 200) {
            System.out.println("User already exists under this data. Enter other data .");
            return;
        }
        System.out.println("User created successfully.");
        System.out.println(GSON.toJson(withList));
    }

    private void logOut() throws IOException, InterruptedException {
        ApiResponse logOut = userController.getLogOut(URL_NAME);
        System.out.println(GSON.toJson(logOut));
    }

    private void logUser(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\w+)\\s{1}(.+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        ApiResponse apiResponse = userController.logsUser(URL_NAME, param);
        if (apiResponse.getCode() != 200) {
            System.out.println("\"User already exists under this data. Enter other data .");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }


    private void getByUserName(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty()) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        user.setUsername(firstWord.get());
        User byUserName = userController.getByUserName(URL_NAME, firstWord.get());
        if (byUserName.getId() == null) {
            System.out.printf("User with %s is not found. Try another name of user.", firstWord.get());
            return;
        }
        System.out.println(GSON.toJson(byUserName));
    }

    @Override
    public void printInstruction() {
        System.out.println("Make your choice please - " + commands);
        System.out.println("If you will choice \"getByUserName\" enter with a space: getByUserName userName. For example: getByUserName string");
        System.out.println("If you will choice \"logUser\" enter with a space: logUser userName password. For example: logUser Petya 12345");
        System.out.println("If you will choice \"logOut\" enter: logOut. For example: logOut");
        System.out.println("If you will choice \"createWithList\" enter: createWithList id userName firstName lastName email password phone userStatus." +
                " For example: createWithList 1 Vikky Mikky Mouse vikky_mouse@gmail.com 12345 123456789 1");
        System.out.println("If you will choice \"createUser\" enter: createUser id userName firstName lastName email password phone userStatus." +
                " For example: createUser 1 Vikky Mikky Mouse vikky_mouse@gmail.com 12345 123456789 1");
        System.out.println("If you will choice \"updateUser\" enter: updateUser userName." +
                " For example: updateUser Vikky");
        System.out.println("If you will choice \"deleteUser\" enter: deleteUser userName." +
                " For example: deleteUser Vikky");
    }
}
