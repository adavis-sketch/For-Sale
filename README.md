# Buy & Sell

Console Java game for four players (you + three AI opponents).

## Run

```bash
cd /path/to/For-Sale
javac -d out src/buysell/*.java
java -cp out buysell.BuyAndSell
```

At the prompt, type `play` to start. After a game finishes, type `again` to play another round. All commands are case-insensitive.

## Rules summary

**Phase 1 — Bidding:** Five rounds of four property cards (numbered 1–20). Players start with $18,000. Each turn the asking price rises by $1,000 (first turn is $1,000). Type `bid` or `pass` (any case). Passing assigns the lowest remaining property; you pay half your committed bid (rounded to the nearest $1,000), or nothing if you never bid. The last player still bidding wins the highest card and pays their full bid.

**Phase 2 — Selling:** Five rounds of four check cards ($0, then $2,000–$10,000 with no $1,000 value, two copies each). Secretly play one property; cards are matched highest-to-lowest with the checks. Total score = remaining cash + sum of checks won.