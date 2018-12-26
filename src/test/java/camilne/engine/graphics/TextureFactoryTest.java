package camilne.engine.graphics;

import camilne.engine.SetupLwjglThreadExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith({SetupLwjglThreadExtension.class})
class TextureFactoryTest {

    @Test
    void canCreateTexture() {
        var texture = TextureFactory.create("texture.png");

        assertNotNull(texture);
        assertTrue(texture.isValid());
    }

    @Test
    void doesReturnNullWhenTextureCannotBeLoaded() {
        var texture = TextureFactory.create("does_not_exist");

        assertNull(texture);
    }

    @Test
    void doesReturnSameTextureWhenSameResource() {
        var texture1 = TextureFactory.create("texture.png");
        var texture2 = TextureFactory.create("texture.png");
        var texture3 = TextureFactory.create("transparent.png");

        assertSame(texture1, texture2);
        assertNotSame(texture1, texture3);
    }

    @Test
    void doesDestroyTexture() {
        var texture1 = TextureFactory.create("texture.png");
        var texture2 = TextureFactory.create("transparent.png");

        TextureFactory.destroy();

        assertNotNull(texture1);
        assertFalse(texture1.isValid());
        assertNotNull(texture2);
        assertFalse(texture2.isValid());
    }
}
