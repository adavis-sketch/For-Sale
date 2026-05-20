package buysell;

public final class PlayerNames {
    private PlayerNames() {
    }

    public static boolean isBlockedName(String name) {
        String lower = name.trim().toLowerCase();
        return lower.equals("micah") || lower.equals("mdeezy") || lower.equals("md")
                || lower.equals("kaden") || lower.equals("henry") || lower.equals("daniel");
    }

    public static String possessive(String name) {
        if (name.endsWith("s") || name.endsWith("S")) {
            return name + "'";
        }
        return name + "'s";
    }
}
