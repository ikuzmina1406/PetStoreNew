package ua.goIt.commands;

import java.io.IOException;
import java.util.regex.Matcher;

import static ua.goIt.commands.Commands.pattern;

public class Command {
    private final Commands commands = new MainMenu();
    private Commands activeCommand = commands;

    public Command() {
        System.out.println("Hello! Please, choice one of the commands, please:" + "\n" +
                "\"start\" - start working at the store \"PetStore\" " + "\n" +
                "\"main\" - to get the main menu of store \"PetStore\"" + "\n" +
                "\"exit\" - exit from the application");
    }

    public void getMainCommand(String params) throws IOException, InterruptedException {
        Matcher firstWord = pattern.matcher(params);
        if (firstWord.find()) {
            String group = firstWord.group();
            switch (group) {

                case "start":
                    System.out.println("Hello, you are at the `PetStore`!");
                case "main":
                    activeCommand = commands;
                    activeCommand.printInstruction();
                    return;

                case "exit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    return;

                default:
                    activeCommand.handle(params, commands1 -> {
                        activeCommand = commands1;
                        activeCommand.printInstruction();
                    });

            }

        }
    }
}
