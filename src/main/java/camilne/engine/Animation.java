package camilne.engine;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private List<TextureRegion> frames;
    private int frameNumber;
    private int frameDuration;
    private int frameTime;
    private boolean isRunning;

    public Animation(List<TextureRegion> frames, int frameDuration) {
        if (frames.isEmpty()) {
            throw new RuntimeException("Creating animation with no frames");
        }

        this.frames = frames;
        this.frameDuration = frameDuration;
        this.frameNumber = 0;
        this.frameTime = 0;
        this.isRunning = false;

        AnimationPool.getInstance().addAnimation(this);
    }

    public Animation(TextureRegion frameSize, int frameCount, int frameDuration) {
        this(createFramesFromStrip(frameSize, frameCount), frameDuration);
    }

    private static List<TextureRegion> createFramesFromStrip(TextureRegion firstFrame, int frameCount) {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 0; i < frameCount; i++) {
            var offset = firstFrame.getX() + firstFrame.getWidth() * i;
            frames.add(new TextureRegion(firstFrame).offset(offset, 0));
        }
        return frames;
    }

    public void advanceFrame() {
        frameTime++;
        if (frameTime >= frameDuration) {
            frameTime = 0;
            frameNumber = (frameNumber + 1) % getFrameCount();
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
        return frames.get(frameNumber);
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
}
