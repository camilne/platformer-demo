package camilne.engine.graphics.font;

public class Kerning {
    private final int firstCodePoint;
    private final int secondCodePoint;
    private final int amount;

    public Kerning(int firstCodePoint, int secondCodePoint, int amount) {
        this.firstCodePoint = firstCodePoint;
        this.secondCodePoint = secondCodePoint;
        this.amount = amount;
    }

    public int getFirstCodePoint() {
        return firstCodePoint;
    }

    public int getSecondCodePoint() {
        return secondCodePoint;
    }

    public int getAmount() {
        return amount;
    }
}
