package buysell;

import java.util.Scanner;

public class BuyAndSell {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            waitForInput(scanner, "Type PLAY to play Buy and Sell", "play");

            while (true) {
                String playerName = readPlayerName(scanner);
                if (PlayerNames.isBlockedName(playerName)) {
                    System.out.println("you lose weirdo");
                    return;
                }
                new Game(scanner, playerName).run();
                waitForInput(scanner, "To play again type AGAIN", "again");
            }
        } finally {
            scanner.close();
        }
    }

    private static String readPlayerName(Scanner scanner) {
        while (true) {
            System.out.println("Enter your name:");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("Name cannot be empty.");
        }
    }

    private static void waitForInput(Scanner scanner, String prompt, String expected) {
        System.out.println(prompt);
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase(expected)) {
                return;
            }
        }
    }
}
