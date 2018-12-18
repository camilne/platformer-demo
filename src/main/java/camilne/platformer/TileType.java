package camilne.platformer;

public enum TileType {
    STONE_LEFT(72, 504, 70, 70),
    STONE_MID(72, 432, 70, 70),
    STONE_RIGHT(72, 360, 70, 70);

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    TileType(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
