package camilne.engine;

import java.util.List;

public class Sprite {

    private Animation animation;
    private float x;
    private float y;
    private float width;
    private float height;

    public Sprite(TextureRegion region, float x, float y, float width, float height) {
        this(new Animation(List.of(region), 0), x, y, width, height);
    }

    public Sprite(Animation animation, float x, float y, float width, float height) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Texture getTexture() {
        return animation.getCurrentFrame().getTexture();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
