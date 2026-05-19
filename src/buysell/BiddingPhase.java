package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BiddingPhase {
    private final List<Player> players;
    private final List<Integer> propertyDeck;
    private final HumanInput humanInput;
    private final Scanner scanner;
    private int startingPlayerIndex;
    private int lastRoundWinnerIndex = -1;

    public BiddingPhase(List<Player> players, List<Integer> propertyDeck, HumanInput humanInput,
                        Scanner scanner) {
        this.players = players;
        this.propertyDeck = propertyDeck;
        this.humanInput = humanInput;
        this.scanner = scanner;
    }

    public boolean play() {
        System.out.println();
        System.out.println("========== PHASE 1: BIDDING ==========");
        System.out.println("Each player starts with $18,000. Five rounds — four properties per round.");
        System.out.println();

        for (int round = 1; round <= 5; round++) {
            if (!playRound(round)) {
                return false;
            }
        }
        return true;
    }

    private boolean playRound(int roundNumber) {
        humanInput.resetBiddingRound();

        List<Integer> shown = drawProperties(4);
        Collections.sort(shown);

        System.out.println("--- Bidding Round " + roundNumber + " of 5 ---");
        System.out.println("Properties up for bid: " + shown);
        System.out.println();

        for (Player player : players) {
            player.resetRoundBid();
        }

        List<Integer> available = new ArrayList<>(shown);
        List<Player> active = new ArrayList<>(players);
        int askingPrice = 1000;
        int seat = startingPlayerIndex;

        while (active.size() > 1) {
            Player current = players.get(seat);
            seat = (seat + 1) % players.size();

            if (!active.contains(current)) {
                continue;
            }

            if (!current.isHuman()) {
                TurnPacing.pauseBeforeAiTurn(scanner, current.getName());
            }

            printAvailableCards(available);
            System.out.println("Asking price this turn: $" + formatMoney(askingPrice));
            System.out.println(current.getName() + " — Money: $" + formatMoney(current.getMoney())
                    + (current.getRoundBid() > 0
                    ? " | Your bid so far: $" + formatMoney(current.getRoundBid())
                    : ""));

            TurnChoice choice = resolveBidChoice(current, askingPrice);
            if (choice == TurnChoice.KICKED) {
                System.out.println();
                System.out.println("You've been kicked for griefing.");
                return false;
            }

            if (choice == TurnChoice.BID) {
                current.setRoundBid(askingPrice);
                if (!current.isHuman()) {
                    System.out.println(current.getName() + " bids at $" + formatMoney(askingPrice) + ".");
                } else {
                    System.out.println("You bid $" + formatMoney(askingPrice) + ".");
                }
            } else {
                int payment = passPayment(current);
                Collections.sort(available);
                int card = available.remove(0);
                current.addProperty(card);
                current.subtractMoney(payment);
                active.remove(current);

                System.out.println(current.getName() + " passes and receives property #" + card
                        + (payment > 0 ? ", paying $" + formatMoney(payment) : ", paying nothing."));
            }

            if (!current.isHuman()) {
                TurnPacing.pauseAfterAiTurn(scanner);
            }

            askingPrice += 1000;
        }

        Player winner = active.get(0);
        if (!winner.isHuman()) {
            TurnPacing.pauseBeforeAiTurn(scanner, winner.getName());
        }

        printAvailableCards(available);
        Collections.sort(available);
        int winningCard = available.get(available.size() - 1);
        int payment = winner.getRoundBid();
        winner.addProperty(winningCard);
        winner.subtractMoney(payment);
        lastRoundWinnerIndex = players.indexOf(winner);
        startingPlayerIndex = (lastRoundWinnerIndex + 1) % players.size();

        System.out.println();
        System.out.println(winner.getName() + " wins property #" + winningCard
                + " and pays $" + formatMoney(payment) + ".");

        if (!winner.isHuman()) {
            TurnPacing.pauseAfterAiTurn(scanner);
        } else {
            System.out.println();
        }

        return true;
    }

    private enum TurnChoice {
        BID, PASS, KICKED
    }

    private TurnChoice resolveBidChoice(Player current, int askingPrice) {
        if (current.isHuman()) {
            HumanInput.Action action = humanInput.readBidOrPass(current, askingPrice);
            if (action == HumanInput.Action.KICKED) {
                return TurnChoice.KICKED;
            }
            if (action == HumanInput.Action.BID) {
                return TurnChoice.BID;
            }
            return TurnChoice.PASS;
        }

        boolean firstTurn = current.getRoundBid() == 0;
        boolean bid = AiLogic.shouldBid(askingPrice, firstTurn);
        if (bid && current.getMoney() < askingPrice) {
            bid = false;
        }
        return bid ? TurnChoice.BID : TurnChoice.PASS;
    }

    private int passPayment(Player player) {
        if (player.getRoundBid() == 0) {
            return 0;
        }
        return DeckUtil.halfBidRounded(player.getRoundBid());
    }

    private List<Integer> drawProperties(int count) {
        List<Integer> drawn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            drawn.add(propertyDeck.remove(0));
        }
        return drawn;
    }

    private static void printAvailableCards(List<Integer> available) {
        List<Integer> sorted = new ArrayList<>(available);
        Collections.sort(sorted);
        if (sorted.size() == 1) {
            System.out.println("Card available: #" + sorted.get(0) + " (goes to the last bidder)");
        } else {
            System.out.println("Cards available: " + sorted
                    + " (lowest #" + sorted.get(0) + " — next passer; highest #"
                    + sorted.get(sorted.size() - 1) + " — last bidder)");
        }
    }

    private static String formatMoney(int amount) {
        return String.format("%,d", amount);
    }

    public int getPhaseTwoStartingPlayerIndex() {
        if (lastRoundWinnerIndex < 0) {
            return 0;
        }
        return (lastRoundWinnerIndex + 1) % players.size();
    }
}
