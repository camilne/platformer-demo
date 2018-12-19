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
        setAnimation(animation);
    }

    public void setAnimation(Animation animation) {
        if (this.animation != null) {
            this.animation.stop();
        }
        this.animation = animation;
        this.animation.start();
    }

    public Animation getAnimation() {
        return animation;
    }

    public Texture getTexture() {
        return animation.getCurrentFrame().getTexture();
    }
}
