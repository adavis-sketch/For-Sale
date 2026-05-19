package buysell;

import java.util.Scanner;

public class BuyAndSell {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            new Game(scanner).run();
        } finally {
            scanner.close();
        }
    }
}
