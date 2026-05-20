package buysell;

public final class TurnPacing {
    private static final int MS_BEFORE_AI_TURN = 2000;
    private static final int MS_AFTER_AI_TURN = 1500;

    private TurnPacing() {
    }

    public static void beforeAiTurn(String playerName) {
        System.out.println();
        System.out.println(playerName + "'s turn...");
        sleep(MS_BEFORE_AI_TURN);
    }

    public static void afterAiTurn() {
        sleep(MS_AFTER_AI_TURN);
        System.out.println();
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
