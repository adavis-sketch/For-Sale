package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class SellingPhase {
    private final List<Player> players;
    private final List<Integer> checkDeck;
    private final HumanInput humanInput;
    private int startingPlayerIndex;

    public SellingPhase(List<Player> players, List<Integer> checkDeck, HumanInput humanInput,
                        int startingPlayerIndex) {
        this.players = players;
        this.checkDeck = checkDeck;
        this.humanInput = humanInput;
        this.startingPlayerIndex = startingPlayerIndex;
    }

    public boolean play() {
        System.out.println();
        System.out.println("========== PHASE 2: SELLING ==========");
        System.out.println("Four checks are shown each round. Secretly choose a property to sell.");
        System.out.println("Highest property pairs with the largest check, and so on.");
        System.out.println();

        for (int round = 1; round <= 5; round++) {
            if (!playRound(round)) {
                return false;
            }
        }
        return true;
    }

    private boolean playRound(int roundNumber) {
        List<Integer> checks = drawChecks(4);
        List<Integer> sortedChecks = new ArrayList<>(checks);
        Collections.sort(sortedChecks);
        int minCheck = sortedChecks.get(0);
        int maxCheck = sortedChecks.get(sortedChecks.size() - 1);

        System.out.println("--- Selling Round " + roundNumber + " of 5 ---");
        System.out.println("Checks this round: " + formatCheckList(checks));
        System.out.println("Choose one property card to play (selections are hidden until all are in).");
        System.out.println();

        Map<Player, Integer> choices = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get((startingPlayerIndex + i) % players.size());
            int chosen;

            if (player.isHuman()) {
                player.sortPropertiesForDisplay();
                Integer choice = humanInput.readPropertyChoice(player);
                if (choice == null) {
                    System.out.println();
                    System.out.println("You've been kicked for griefing.");
                    return false;
                }
                chosen = choice;
            } else {
                chosen = AiLogic.chooseProperty(player.getProperties(), minCheck, maxCheck);
            }

            if (!player.removeProperty(chosen)) {
                System.out.println(player.getName() + " could not play property #" + chosen + ".");
                return false;
            }
            choices.put(player, chosen);
            System.out.println(player.getName() + " has locked in a property.");
        }

        System.out.println();
        System.out.println("--- Reveal ---");
        for (Player player : players) {
            System.out.println(player.getName() + " played property #" + choices.get(player));
        }

        List<Player> ranking = new ArrayList<>(players);
        ranking.sort(Comparator.comparingInt((Player p) -> choices.get(p)).reversed());

        List<Integer> checksHighToLow = new ArrayList<>(checks);
        checksHighToLow.sort(Comparator.reverseOrder());

        for (int i = 0; i < ranking.size(); i++) {
            Player player = ranking.get(i);
            int check = checksHighToLow.get(i);
            player.addCheck(check);
            System.out.println(player.getName() + " (#" + choices.get(player) + ") receives check: $"
                    + formatMoney(check));
        }

        startingPlayerIndex = (startingPlayerIndex + 1) % players.size();
        System.out.println();
        return true;
    }

    private List<Integer> drawChecks(int count) {
        List<Integer> drawn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            drawn.add(checkDeck.remove(0));
        }
        return drawn;
    }

    private static String formatMoney(int amount) {
        return String.format("%,d", amount);
    }

    private static String formatCheckList(List<Integer> checks) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < checks.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("$").append(formatMoney(checks.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
}
