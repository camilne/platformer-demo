package camilne.engine;

import camilne.engine.graphics.Animation;
import camilne.engine.graphics.Texture;
import camilne.engine.graphics.TextureRegion;

import java.util.List;

public class Sprite extends GameObject {

    private Animation animation;

    public Sprite(TextureRegion region, float x, float y, float width, float height) {
        this(new Animation(List.of(region), 0), x, y, width, height);
    }

    public Sprite(Animation animation, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.animation = animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Texture getTexture() {
        return animation.getCurrentFrame().getTexture();
    }
}
