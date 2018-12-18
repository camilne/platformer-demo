package camilne.engine.graphics;

import camilne.engine.SetupLwjglThreadExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SetupLwjglThreadExtension.class})
class TextureFactoryTest {

    @Test
    void canCreateTexture() throws Exception {
        var texture = TextureFactory.create("texture.png");

        assertNotNull(texture);
    }

    @Test
    void doesReturnSameTextureWhenSameResource() throws Exception {
        var texture1 = TextureFactory.create("texture.png");
        var texture2 = TextureFactory.create("texture.png");
        var texture3 = TextureFactory.create("transparent.png");

        assertSame(texture1, texture2);
        assertNotSame(texture1, texture3);
    }
}
