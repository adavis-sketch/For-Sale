package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class AiLogic {
    private static final Random RANDOM = new Random();
    private static final int STARTING_MONEY = 18_000;
    private static final int MAX_CARD_VALUE = 20;

    private AiLogic() {
    }

    /**
     * Bid chance from five factors: highest card, lowest card, spread, cash on hand,
     * and current asking price (higher price lowers willingness).
     */
    public static boolean shouldBid(int askingPrice, int money, List<Integer> availableCards) {
        if (money < askingPrice) {
            return false;
        }

        List<Integer> sorted = new ArrayList<>(availableCards);
        Collections.sort(sorted);
        int lowest = sorted.get(0);
        int highest = sorted.get(sorted.size() - 1);
        int spread = highest - lowest;

        double highFactor = (highest - 1.0) / (MAX_CARD_VALUE - 1);
        double lowFactor = (MAX_CARD_VALUE - lowest) / (double) MAX_CARD_VALUE;
        double spreadFactor = spread / (double) (MAX_CARD_VALUE - 1);
        double moneyFactor = Math.min(1.0, money / (double) STARTING_MONEY);
        double priceFactor = Math.min(1.0, askingPrice / (double) STARTING_MONEY);

        double chance = 0.50
                + highFactor * 0.13
                + lowFactor * 0.09
                + spreadFactor * 0.11
                + moneyFactor * 0.11
                - priceFactor * 0.25;

        chance += (RANDOM.nextDouble() - 0.5) * 0.08;
        chance = Math.max(0.18, Math.min(0.88, chance));

        return RANDOM.nextDouble() < chance;
    }

    public static int chooseProperty(List<Integer> owned, int minCheck, int maxCheck) {
        List<Integer> sorted = new ArrayList<>(owned);
        Collections.sort(sorted);
        double pressure = 0.0;
        pressure += (maxCheck / 10000.0) * 0.45;
        pressure += (1.0 - (minCheck / 10000.0)) * 0.35;
        pressure = Math.max(0.0, Math.min(1.0, pressure));

        int index;
        if (RANDOM.nextDouble() < pressure) {
            int topStart = Math.max(0, sorted.size() - 2);
            index = topStart + RANDOM.nextInt(sorted.size() - topStart);
        } else {
            index = RANDOM.nextInt(Math.min(3, sorted.size()));
        }
        return sorted.get(index);
    }
}
