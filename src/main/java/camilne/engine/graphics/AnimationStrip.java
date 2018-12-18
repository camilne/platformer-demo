package camilne.engine.graphics;

public class AnimationStrip {

    private final TextureRegion firstFrame;
    private final int spacing;
    private final int frameCount;

    public AnimationStrip(TextureRegion firstFrame, int spacing, int frameCount) {
        this.firstFrame = firstFrame;
        this.spacing = spacing;
        this.frameCount = frameCount;
    }

    public TextureRegion getFirstFrame() {
        return firstFrame;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getFrameCount() {
        return frameCount;
    }
}
