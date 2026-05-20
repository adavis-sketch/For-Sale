package buysell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static final int STARTING_MONEY = 18_000;

    private final Scanner scanner;
    private final HumanInput humanInput;
    private final List<Player> players;

    public Game(Scanner scanner) {
        this.scanner = scanner;
        this.humanInput = new HumanInput(scanner);
        this.players = createPlayers();
    }

    public void run() {
        printWelcome();

        List<Integer> propertyDeck = DeckUtil.createPropertyDeck();
        List<Integer> checkDeck = DeckUtil.createCheckDeck();

        BiddingPhase bidding = new BiddingPhase(players, propertyDeck, humanInput);
        if (!bidding.play()) {
            return;
        }

        humanInput.resetGriefingBetweenPhases();

        SellingPhase selling = new SellingPhase(players, checkDeck, humanInput,
                bidding.getPhaseTwoStartingPlayerIndex());
        if (!selling.play()) {
            return;
        }

        printFinalStandings();
    }

    private List<Player> createPlayers() {
        List<Player> list = new ArrayList<>();
        list.add(new Player("You", true, STARTING_MONEY));
        list.add(new Player("AI 1", false, STARTING_MONEY));
        list.add(new Player("AI 2", false, STARTING_MONEY));
        list.add(new Player("AI 3", false, STARTING_MONEY));
        return list;
    }

    private void printWelcome() {
        System.out.println("========================================");
        System.out.println("            BUY & SELL");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Four players compete in two phases.");
        System.out.println("Phase 1: Bid on properties. Phase 2: Sell properties for checks.");
        System.out.println("The richest total (cash + checks) wins.");
        System.out.println();
    }

    private void printFinalStandings() {
        System.out.println();
        System.out.println("========== FINAL STANDINGS ==========");

        List<Player> ranked = new ArrayList<>(players);
        ranked.sort(Comparator.comparingInt(Player::totalWealth).reversed());

        int place = 1;
        for (Player player : ranked) {
            int checkSum = player.getChecks().stream().mapToInt(Integer::intValue).sum();
            System.out.println(place + ". " + player.getName()
                    + " — Total: $" + formatMoney(player.totalWealth())
                    + " (Cash: $" + formatMoney(player.getMoney())
                    + ", Checks: $" + formatMoney(checkSum) + ")");
            if (!player.getChecks().isEmpty()) {
                System.out.println("   Checks collected: " + player.getChecks());
            }
            place++;
        }

        System.out.println();
        if (ranked.get(0).isHuman()) {
            System.out.println("Congratulations — you win!");
        } else {
            System.out.println(ranked.get(0).getName() + " wins the game.");
        }
    }

    private static String formatMoney(int amount) {
        return String.format("%,d", amount);
    }
}
