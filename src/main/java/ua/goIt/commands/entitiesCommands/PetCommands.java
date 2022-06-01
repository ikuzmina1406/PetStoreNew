package ua.goIt.commands.entitiesCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.goIt.commands.Commands;
import ua.goIt.model.ApiResponse;
import ua.goIt.model.Category;
import ua.goIt.model.Pet;
import ua.goIt.model.Tag;
import ua.goIt.controller.PetController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class PetCommands implements Commands {
    private final Pet pet = new Pet();
    private final PetController petController = new PetController();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String URL_NAME = "https://petstore.swagger.io/v2/pet";
    private final List<String> commands = Arrays.asList("\"getPetById\"", "\"getPetsByStatus\"", "\"deletePetById\"",
            "\"updatePet\"", "\"createPet\"", "\"updatePetsStatus\", \"postUploadImage\"");

    @Override
    public void handle(String param, Consumer<Commands> consumer) throws IOException, InterruptedException {
        String[] words = param.split(" ");
        String newParam = param.replace(words[0], "").trim();
        switch (words[0].toLowerCase(Locale.ROOT)) {
            case "getpetbyid":
                getPetById(newParam);
                return;
            case "getpetsbystatus":
                getPetsByStatus(newParam);
                return;
            case "deletepetbyid":
                deletePetById(newParam);
                return;
            case "updatepet":
                updatePet(newParam);
                return;
            case "createpet":
                createPet(newParam);
                return;
            case "updatepetsstatus":
                updatePetsStatus(newParam);
                return;
            case "postuploadimage":
                postUploadImage(newParam);
        }

    }

    private void updatePetsStatus(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setStatus(words[2]);
        ApiResponse apiResponse = petController.updatePetsStatus(URL_NAME, pet);
        System.out.println(GSON.toJson(apiResponse));
    }

    private void createPet(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\w+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setCategory(new Category(Long.parseLong(words[2]), words[3]));
        pet.setStatus(words[4]);
        pet.setPhotoUrls(new String[]{words[5]});
        pet.setTags(new Tag[]{new Tag(Long.parseLong(words[6]), words[7])});
        Pet createdPet = petController.createPet(URL_NAME, pet);
        System.out.println(GSON.toJson(createdPet));
    }

    private void updatePet(String param) throws IOException, InterruptedException {
        if (!param.matches("^(\\d+)\\s{1}(\\w+)\\s{1}(\\d+)\\s{1}(\\w+)\\s{1}(\\w+)\\s{1}(.+)\\s{1}(\\d+)\\s{1}(\\w+)$")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        String[] words = param.split(" ");
        pet.setId(Long.parseLong(words[0]));
        pet.setName(words[1]);
        pet.setCategory(new Category(Long.parseLong(words[2]), words[3]));
        pet.setStatus(words[4]);
        pet.setPhotoUrls(new String[]{words[5]});
        pet.setTags(new Tag[]{new Tag(Long.parseLong(words[6]), words[7])});
        Pet updatedPet = petController.updatePet(URL_NAME, pet);
        System.out.println(GSON.toJson(updatedPet));

    }

    private void deletePetById(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        ApiResponse apiResponse = petController.deletePetById(URL_NAME, String.valueOf(Integer.parseInt(firstWord.get())));
        if (apiResponse == null) {
            System.out.printf("Pet with id %s is not found. Enter another id , please .", firstWord.get());
            return;
        }
        System.out.printf("Pet with  id %s was deleted .\n", firstWord.get());
        System.out.println(GSON.toJson(apiResponse));

    }

    private void getPetsByStatus(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        List<Pet> petsByStatus = petController.getPetsByStatus(URL_NAME, firstWord.get());
        petsByStatus.forEach(s -> System.out.println(GSON.toJson(s)));

    }

    private void getPetById(String param) throws IOException, InterruptedException {
        Optional<String> firstWord = getFirstWord(param);
        if (firstWord.isEmpty() || !firstWord.get().matches("[-+]?\\d+")) {
            System.out.println("Your input could not be processed, try again .");
            return;
        }
        Pet petById = petController.getPetById(URL_NAME, String.valueOf(Integer.parseInt((firstWord.get()))));
        if (petById.getId() == null) {
            System.out.printf("Pet with id %s is not found. Enter another id , please .", firstWord.get());
            return;
        }
        System.out.println(GSON.toJson(petById));
    }

    private void postUploadImage(String param) throws IOException, InterruptedException {
        String[] words = param.split(" ");
        if (words.length != 3) {
            System.out.println("our input could not be processed, try again .");
            return;
        }
        ApiResponse apiResponse = petController.postUploadImage(URL_NAME, param);
        if (apiResponse.getCode() != 200) {
            System.out.println("our input could not be processed, try again .");
            return;
        }
        System.out.println(GSON.toJson(apiResponse));
    }

    @Override
    public void printInstruction() {
        System.out.println("Make your choice please - " + commands);
        System.out.println("If you will choice \"getPetById\" enter with a space: getPetById (id - the pet you want to get). For example: getPetById 1");
        System.out.println("If you will choice\"getPetsByStatus\" enter with a space: getPetsByStatus (status - the pet you want to get)." +
                "Pets have 3 statuses: {available, pending, sold}. For example: getPetsByStatus sold");
        System.out.println("If you will choice \"deletePetById\" enter with a space: " +
                "deletePetById (id - the pet you want to delete). For example: deletePetById 1");
        System.out.println("If you will choice \"updatePet\" enter with a space: " +
                "updatePet (id - the pet you want to change ) name categoryId categoryName petStatus photoUrl tagId tagName" +
                "\n For example: updatePet 1 Terry 1 dog available https://krasivosti.pro/sobaki/10852-labrador-chernyj-devochka.html 1 sweet");
        System.out.println("If you will choice \"createPet\" enter with a space: " +
                "createPet (id - the pet you want to create) name categoryId categoryName petStatus photoUrl tagId tagName" +
                "\nFor example: createPet 1 Terry 1 dog available https://obyava.ua/ru/shchenki-labradora-v-kieve-ot-krasivoy-pary-pitomnik-1702407.html 1 sweet");
        System.out.println("If you will choice \"updatePetsStatus\"  enter with a space: " +
                "updatePetsStatus (id - the pet you want to update) name petStatus" +
                " For example: updatePetsStatus 1 Max available");
        System.out.println("If you will choice \"postUploadImage\"  enter with a space: " +
                "postUploadImage (id - the pet you want to download the file)" +
                "  For example: postUploadImage 1 MyFavourite https://focusedcollection.com/ru/309807082/stock-photo-black-labrador-puppy-looking-camera.html");
    }
}
