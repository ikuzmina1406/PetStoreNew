package ua.goIt;


import ua.goIt.commands.Command;

import java.io.IOException;
import java.util.Scanner;

public class App
{
    public static void main(String[] args) throws IOException, InterruptedException {
            Scanner scanner = new Scanner(System.in);
            Command command = new Command();
            while (scanner.hasNext()) {
                command.getMainCommand(scanner.nextLine());
            }

        }
    }

