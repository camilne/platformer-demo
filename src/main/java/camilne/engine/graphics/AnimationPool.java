package camilne.engine.graphics;

import java.util.ArrayList;
import java.util.List;

public class AnimationPool {

    private static AnimationPool instance;

    private List<Animation> animations;

    private AnimationPool() {
        animations = new ArrayList<>();
    }

    public synchronized static AnimationPool getInstance() {
        if (instance == null) {
            instance = new AnimationPool();
        }
        return instance;
    }

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public void removeAnimation(Animation animation) {
        animations.remove(animation);
    }

    public void update() {
        for (var animation : animations) {
            if (animation.isRunning()) {
                animation.advanceFrame();
            }
        }
    }

}
