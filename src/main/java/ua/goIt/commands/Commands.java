package ua.goIt.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Commands {
    Pattern pattern = Pattern.compile("^\\w+");


    void handle(String param, Consumer<Commands> consumer) throws IOException, InterruptedException;

    default Optional<String> getFirstWord(String params) {
        Matcher findFirst = pattern.matcher(params);
        if (findFirst.find()) {
            String group = findFirst.group();
            return Optional.of(group);
        }
        return Optional.empty();
    }

    void printInstruction();
}