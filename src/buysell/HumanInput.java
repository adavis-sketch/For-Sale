package buysell;

import java.util.Scanner;

public class HumanInput {
    private final Scanner scanner;
    private int consecutiveInvalid;
    private int roundsWithAutoPass;

    public HumanInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public enum Action {
        BID,
        PASS,
        AUTO_PASS,
        KICKED
    }

    public Action readBidOrPass(Player player, int askingPrice) {
        while (true) {
            System.out.print("Type 'bid' or 'pass': ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("bid")) {
                if (player.getMoney() < askingPrice) {
                    System.out.println("You do not have enough money. You must pass.");
                    if (handleInvalid(player)) {
                        return Action.KICKED;
                    }
                    continue;
                }
                player.resetInvalidInputStreak();
                consecutiveInvalid = 0;
                return Action.BID;
            }

            if (line.equalsIgnoreCase("pass")) {
                player.resetInvalidInputStreak();
                consecutiveInvalid = 0;
                return Action.PASS;
            }

            System.out.println("Invalid statement. Type 'bid' or 'pass'.");
            if (handleInvalid(player)) {
                return Action.KICKED;
            }
        }
    }

    public Integer readPropertyChoice(Player player) {
        player.sortPropertiesForDisplay();
        System.out.println("Your properties: " + player.getProperties());
        System.out.print("Enter the property value you want to play: ");

        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (player.getProperties().contains(value)) {
                    player.resetInvalidInputStreak();
                    consecutiveInvalid = 0;
                    return value;
                }
                System.out.println("You do not own that property. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid statement. Enter a property number you own.");
            }

            if (handleInvalid(player)) {
                return null;
            }
            System.out.print("Enter the property value you want to play: ");
        }
    }

    private boolean handleInvalid(Player player) {
        consecutiveInvalid++;
        player.recordInvalidInput();
        if (consecutiveInvalid >= 3) {
            consecutiveInvalid = 0;
            roundsWithAutoPass++;
            System.out.println("Three invalid attempts in a row — you are automatically passing.");
            return roundsWithAutoPass >= 2;
        }
        return false;
    }

    public boolean wasKicked() {
        return roundsWithAutoPass >= 2;
    }

    public void resetGriefingBetweenPhases() {
        roundsWithAutoPass = 0;
        consecutiveInvalid = 0;
    }
}
