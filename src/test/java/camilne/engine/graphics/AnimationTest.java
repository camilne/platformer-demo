package camilne.engine.graphics;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnimationTest {

    @Test
    void testConstructor() {
        var firstFrame = Mockito.mock(TextureRegion.class);
        var regions = List.of(firstFrame, Mockito.mock(TextureRegion.class));
        var animation = new Animation(regions, 6);

        assertSame(firstFrame, animation.getCurrentFrame());
        assertEquals(2, animation.getFrameCount());
        assertEquals(0, animation.getFrameNumber());
        assertFalse(animation.isRunning());
        assertFalse(animation.isFlipX());
        assertTrue(animation.isShouldLoop());
    }

    @Test
    void canAdvanceFrame() {
        var regions = List.of(Mockito.mock(TextureRegion.class), Mockito.mock(TextureRegion.class));
        var animation = new Animation(regions, 2);

        animation.advanceFrame();
        assertEquals(0, animation.getFrameNumber());

        animation.advanceFrame();
        assertEquals(1, animation.getFrameNumber());

        animation.advanceFrame();
        assertEquals(1, animation.getFrameNumber());

        animation.advanceFrame();
        assertEquals(0, animation.getFrameNumber());
    }

    @Test
    void doesSetDonePropertyWhenFinished() {
        var regions = List.of(Mockito.mock(TextureRegion.class), Mockito.mock(TextureRegion.class));
        var animation = new Animation(regions, 1);
        animation.setShouldLoop(false);

        assertFalse(animation.doneProperty().get());
        animation.advanceFrame();
        assertFalse(animation.doneProperty().get());
        animation.advanceFrame();
        assertTrue(animation.doneProperty().get());
    }

    @Test
    void doesNotSetDonePropertyWhenLooping() {
        var regions = List.of(Mockito.mock(TextureRegion.class), Mockito.mock(TextureRegion.class));
        var animation = new Animation(regions, 1);

        assertFalse(animation.doneProperty().get());
        animation.advanceFrame();
        assertFalse(animation.doneProperty().get());
        animation.advanceFrame();
        assertFalse(animation.doneProperty().get());
    }

    @Test
    void canSetFlipX() {
        var frame = Mockito.mock(TextureRegion.class);
        Mockito.doCallRealMethod().when(frame).setFlipX(Mockito.anyBoolean());
        when(frame.isFlipX()).thenCallRealMethod();
        var regions = List.of(frame);
        var animation = new Animation(regions, 1);
        animation.setFlipX(true);

        assertTrue(animation.isFlipX());
        assertTrue(animation.getCurrentFrame().isFlipX());
    }

    @Test
    void doesThrowExceptionWhenCreatingAnimationWithNoFrames() {
        assertThrows(RuntimeException.class, () -> new Animation(List.of(), 1));
    }
}
