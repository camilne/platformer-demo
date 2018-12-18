package camilne.engine;

public class SpriteEntry {

    private final Sprite sprite;
    private final float zIndex;

    public SpriteEntry(Sprite sprite, float zIndex) {
        this.sprite = sprite;
        this.zIndex = zIndex;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getZIndex() {
        return zIndex;
    }
}
