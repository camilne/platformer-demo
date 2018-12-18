package camilne.engine.graphics;

import camilne.engine.SetupLwjglThreadExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({SetupLwjglThreadExtension.class})
class TextureTest {

    @Test
    void testSize() throws Exception {
        try (var texture = new Texture("texture.png")) {
            assertEquals(4, texture.getWidth());
            assertEquals(2, texture.getHeight());
        }

        try (var texture = new Texture("texture.jpg")) {
            assertEquals(1, texture.getWidth());
            assertEquals(1, texture.getHeight());
        }
    }

    @Test
    void testComponents() throws Exception {
        try (var texture = new Texture("texture.png")) {
            assertEquals(3, texture.getComponents());
        }

        try (var texture = new Texture("transparent.png")) {
            assertEquals(4, texture.getComponents());
        }

        try (var texture = new Texture("texture.jpg")) {
            assertEquals(3, texture.getComponents());
        }
    }

    @Test
    void throwsFileNotFoundExceptionIfFileDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> new Texture("does_not_exist.png"));
    }

}
