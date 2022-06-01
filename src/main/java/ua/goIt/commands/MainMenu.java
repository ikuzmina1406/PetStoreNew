package ua.goIt.commands;


import ua.goIt.commands.entitiesCommands.OrderCommands;
import ua.goIt.commands.entitiesCommands.PetCommands;
import ua.goIt.commands.entitiesCommands.UserCommands;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MainMenu implements Commands {

    private final Map<String, Commands> commandsMap = Map.of(
            "pet", new PetCommands(),
            "store", new OrderCommands(),
            "user", new UserCommands()
    );


    @Override
    public void handle(String param, Consumer<Commands> consumer) {
        Optional<String> firstWord = getFirstWord(param);
        firstWord.map(commandsMap::get).ifPresent(commands ->  {
            consumer.accept(commands);
            try {
                commands.handle(param.replace(firstWord.get(), "").trim(), consumer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void printInstruction() {
        System.out.println(" Enter one of the entities, please," +
                " - [\"pet\", \"store\", \"user\"]");
    }
}