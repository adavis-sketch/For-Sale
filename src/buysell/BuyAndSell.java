package buysell;

import java.util.Scanner;

public class BuyAndSell {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            if (!waitForPlay(scanner)) {
                return;
            }

            while (true) {
                String playerName = readPlayerName(scanner);
                if (PlayerNames.isBlockedName(playerName)) {
                    System.out.println("you lose weirdo");
                    return;
                }
                new Game(scanner, playerName).run();
                if (!askPlayAgain(scanner)) {
                    return;
                }
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

    private static boolean waitForPlay(Scanner scanner) {
        System.out.println("Type PLAY to play Buy and Sell");
        int consecutiveInvalid = 0;
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("play")) {
                return true;
            }
            consecutiveInvalid++;
            System.out.println("Invalid statement. Type 'play' to play.");
            if (consecutiveInvalid >= 3) {
                System.out.println("you lose weirdo");
                return false;
            }
        }
    }

    private static boolean askPlayAgain(Scanner scanner) {
        System.out.println();
        System.out.println("Play again? (yes/no)");
        int consecutiveInvalid = 0;
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("y")) {
                System.out.println();
                return true;
            }
            if (line.equalsIgnoreCase("no") || line.equalsIgnoreCase("n")) {
                return false;
            }
            consecutiveInvalid++;
            System.out.println("Invalid statement. Type 'yes' or 'no'.");
            if (consecutiveInvalid >= 3) {
                return false;
            }
        }
    }
}
