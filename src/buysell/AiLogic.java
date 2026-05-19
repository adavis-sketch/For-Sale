package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class AiLogic {
    private static final Random RANDOM = new Random();

    private AiLogic() {
    }

    public static boolean shouldBid(int askingPrice, boolean firstTurnOfRound) {
        double baseChance = firstTurnOfRound ? 0.82 : 0.55;
        int stepsAboveBase = Math.max(0, (askingPrice / 1000) - 1);
        double chance = baseChance - (stepsAboveBase * 0.12);
        chance = Math.max(0.08, Math.min(0.95, chance));
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
