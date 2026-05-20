package buysell;

import java.util.Scanner;

public class HumanInput {
    private final Scanner scanner;
    private int consecutiveInvalid;
    private int roundsWithAutoPass;
    private boolean forcedPassThisBiddingRound;

    public HumanInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public enum Action {
        BID,
        PASS,
        KICKED
    }

    public void resetBiddingRound() {
        forcedPassThisBiddingRound = false;
    }

    public Action readBidOrPass(Player player, int askingPrice) {
        String name = player.getName();
        if (forcedPassThisBiddingRound) {
            System.out.println(name + " must pass — automatically passed earlier this round.");
            return Action.PASS;
        }

        while (true) {
            System.out.print("Type 'bid' or 'pass': ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("bid")) {
                if (player.getMoney() < askingPrice) {
                    System.out.println(name + " does not have enough money and must pass.");
                    Action result = handleInvalidStrike(player, true);
                    if (result != null) {
                        return result;
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
            Action result = handleInvalidStrike(player, true);
            if (result != null) {
                return result;
            }
        }
    }

    public Integer readPropertyChoice(Player player) {
        String name = player.getName();
        player.sortPropertiesForDisplay();
        System.out.println(PlayerNames.possessive(name) + " properties: " + player.getProperties());
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
                System.out.println(name + " does not own that property. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid statement. Enter a property number you own.");
            }

            Action result = handleInvalidStrike(player, false);
            if (result == Action.KICKED) {
                return null;
            }
            if (result == Action.PASS) {
                player.sortPropertiesForDisplay();
                int lowest = player.getProperties().get(0);
                System.out.println("Automatically playing " + PlayerNames.possessive(name)
                        + " lowest property (#" + lowest + ").");
                return lowest;
            }
            System.out.print("Enter the property value you want to play: ");
        }
    }

    private Action handleInvalidStrike(Player player, boolean biddingRound) {
        consecutiveInvalid++;
        player.recordInvalidInput();
        if (consecutiveInvalid < 3) {
            return null;
        }

        consecutiveInvalid = 0;
        roundsWithAutoPass++;
        if (biddingRound) {
            forcedPassThisBiddingRound = true;
            System.out.println("Three invalid attempts in a row — " + player.getName()
                    + " is automatically passing.");
        } else {
            System.out.println("Three invalid attempts in a row — a property is chosen for "
                    + player.getName() + ".");
        }

        if (roundsWithAutoPass >= 2) {
            return Action.KICKED;
        }
        return Action.PASS;
    }

    public void resetGriefingBetweenPhases() {
        roundsWithAutoPass = 0;
        consecutiveInvalid = 0;
    }
}
