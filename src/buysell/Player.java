package buysell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private final boolean human;
    private int money;
    private final List<Integer> properties = new ArrayList<>();
    private final List<Integer> checks = new ArrayList<>();
    private int roundBid;
    private int consecutiveInvalidInputs;

    public Player(String name, boolean human, int startingMoney) {
        this.name = name;
        this.human = human;
        this.money = startingMoney;
    }

    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return human;
    }

    public int getMoney() {
        return money;
    }

    public void subtractMoney(int amount) {
        money -= amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public List<Integer> getProperties() {
        return properties;
    }

    public List<Integer> getChecks() {
        return checks;
    }

    public int getRoundBid() {
        return roundBid;
    }

    public void setRoundBid(int roundBid) {
        this.roundBid = roundBid;
    }

    public void resetRoundBid() {
        roundBid = 0;
    }

    public void addProperty(int card) {
        properties.add(card);
    }

    public void addCheck(int check) {
        checks.add(check);
    }

    public boolean removeProperty(int card) {
        return properties.remove(Integer.valueOf(card));
    }

    public int consecutiveInvalidInputs() {
        return consecutiveInvalidInputs;
    }

    public void recordInvalidInput() {
        consecutiveInvalidInputs++;
    }

    public void resetInvalidInputStreak() {
        consecutiveInvalidInputs = 0;
    }

    public int totalWealth() {
        int checkTotal = 0;
        for (int check : checks) {
            checkTotal += check;
        }
        return money + checkTotal;
    }

    public void sortPropertiesForDisplay() {
        Collections.sort(properties);
    }

    @Override
    public String toString() {
        return name;
    }
}
