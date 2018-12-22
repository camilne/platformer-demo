package camilne.engine.graphics.font;

public class Insets {
    private int top;
    private int right;
    private int bottom;
    private int left;

    public Insets(int amount) {
        top = amount;
        right = amount;
        bottom = amount;
        left = amount;
    }

    public Insets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }
}
