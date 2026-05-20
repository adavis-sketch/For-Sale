package buysell;

import java.util.Scanner;

public class BuyAndSell {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            waitForInput(scanner, "Type PLAY to play Buy and Sell", "play");

            while (true) {
                new Game(scanner).run();
                waitForInput(scanner, "To play again type AGAIN", "again");
            }
        } finally {
            scanner.close();
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
