package camilne.engine.graphics;

import camilne.engine.general.Property;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private List<TextureRegion> frames;
    private int frameNumber;
    private int frameDuration;
    private int frameTime;
    private boolean isRunning;
    private boolean flipX;
    private boolean shouldLoop;
    private Property<Boolean> done;

    public Animation(List<TextureRegion> frames, int frameDuration) {
        if (frames.isEmpty()) {
            throw new RuntimeException("Creating animation with no frames");
        }

        this.frames = frames;
        this.frameDuration = frameDuration;
        this.frameNumber = 0;
        this.frameTime = 0;
        this.isRunning = false;
        this.shouldLoop = true;
        done = new Property<>(false);

        AnimationPool.getInstance().addAnimation(this);
    }

    public Animation(AnimationStrip strip, int frameDuration) {
        this(createFramesFromStrip(strip), frameDuration);
    }

    private static List<TextureRegion> createFramesFromStrip(AnimationStrip strip) {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 0; i < strip.getFrameCount(); i++) {
            var offset = (strip.getFirstFrame().getWidth() + strip.getSpacing()) * i;
            frames.add(new TextureRegion(strip.getFirstFrame()).offset(offset, 0));
        }
        return frames;
    }

    public void advanceFrame() {
        frameTime++;
        if (frameTime >= frameDuration) {
            frameTime = 0;
            frameNumber++;
            if (frameNumber >= getFrameCount()) {
                frameNumber = 0;
                if (!shouldLoop) {
                    done.set(true);
                }
            }
        }
    }

    public void start() {
        frameNumber = 0;
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public TextureRegion getCurrentFrame() {
        var result = frames.get(frameNumber);
        result.setFlipX(flipX);
        return result;
    }

    public int getFrameCount() {
        return frames.size();
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public boolean isShouldLoop() {
        return shouldLoop;
    }

    public void setShouldLoop(boolean shouldLoop) {
        this.shouldLoop = shouldLoop;
    }

    public Property<Boolean> doneProperty() {
        return done;
    }
}
