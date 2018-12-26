package camilne.engine;

import camilne.engine.graphics.Animation;
import camilne.engine.graphics.Texture;
import camilne.engine.graphics.TextureRegion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SpriteTest {

    @Test
    void doesSetPhysicalAttributes() {
        var texture = Mockito.mock(Texture.class);
        var sprite = new Sprite(new TextureRegion(texture), 12, 8, 4, 3);

        assertEquals(12, sprite.getX());
        assertEquals(8, sprite.getY());
        assertEquals(4, sprite.getWidth());
        assertEquals(3, sprite.getHeight());
    }

    @Test
    void canCreateFromSingleFrame() {
        var texture = Mockito.mock(Texture.class);
        var sprite = new Sprite(new TextureRegion(texture), 0, 0, 1, 1);

        assertEquals(1, sprite.getAnimation().getFrameCount());
        assertEquals(texture, sprite.getTexture());
    }

    @Test
    void canCreateFromAnimation() {
        var animation = Mockito.mock(Animation.class);
        Mockito.doCallRealMethod().when(animation).start();
        when(animation.isRunning()).thenCallRealMethod();
        var sprite = new Sprite(animation, 0, 0, 1, 1);

        assertTrue(sprite.getAnimation().isRunning());
    }

    @Test
    void canSetAnimationWhenAnimationExists() {
        var animation = Mockito.mock(Animation.class);
        Mockito.doCallRealMethod().when(animation).start();
        Mockito.doCallRealMethod().when(animation).stop();
        when(animation.isRunning()).thenCallRealMethod();
        var sprite = new Sprite(animation, 0, 0, 1, 1);

        var animation2 = Mockito.mock(Animation.class);
        Mockito.doCallRealMethod().when(animation2).start();
        when(animation2.isRunning()).thenCallRealMethod();
        sprite.setAnimation(animation2);

        assertEquals(animation2, sprite.getAnimation());
        assertFalse(animation.isRunning());
        assertTrue(animation2.isRunning());
    }
}
