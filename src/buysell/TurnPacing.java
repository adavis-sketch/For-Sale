package buysell;

import java.util.Scanner;

public final class TurnPacing {
    private TurnPacing() {
    }

    public static void pauseBeforeAiTurn(Scanner scanner, String playerName) {
        System.out.println();
        System.out.println(playerName + "'s turn. Press Enter to continue...");
        scanner.nextLine();
    }

    public static void pauseAfterAiTurn(Scanner scanner) {
        System.out.println("Press Enter for the next turn...");
        scanner.nextLine();
        System.out.println();
    }
}
