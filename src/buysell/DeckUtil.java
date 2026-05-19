package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DeckUtil {
    private DeckUtil() {
    }

    public static List<Integer> createPropertyDeck() {
        List<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);
        return deck;
    }

    /** Checks: $0, then $2k–$10k (no $1k), two copies each — 20 cards. */
    public static List<Integer> createCheckDeck() {
        List<Integer> deck = new ArrayList<>();
        deck.add(0);
        deck.add(0);
        for (int value = 2000; value <= 10000; value += 1000) {
            deck.add(value);
            deck.add(value);
        }
        Collections.shuffle(deck);
        return deck;
    }

    public static int halfBidRounded(int bid) {
        if (bid <= 0) {
            return 0;
        }
        double half = bid / 2.0;
        return (int) (Math.round(half / 1000.0) * 1000);
    }
}
