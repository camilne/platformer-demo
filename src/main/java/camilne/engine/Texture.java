package camilne.engine;

import org.lwjgl.system.MemoryStack;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Closeable {

    private final int id;
    private final int width;
    private final int height;
    private final int components;

    public Texture(String path) throws IOException {
        var buffer = IOUtil.ioResourceToByteBuffer(path, 8 * 1024);

        try (var stack = MemoryStack.stackPush()) {
            var width = stack.mallocInt(1);
            var height = stack.mallocInt(1);
            var components = stack.mallocInt(1);

            if (!stbi_info_from_memory(buffer, width, height, components)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            }

            this.width = width.get(0);
            this.height = height.get(0);
            this.components = components.get(0);

            var imageData = stbi_load_from_memory(buffer, width, height, components, 0);
            if (imageData == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }
            this.id = createOpenGLTexture(imageData, this.width, this.height, this.components);
        }
    }

    private static int createOpenGLTexture(ByteBuffer imageData, int width, int height, int components) {
        var id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, components == 3 ? GL_RGB : GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, imageData);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glBindTexture(GL_TEXTURE_2D, 0);
        return id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void close() {
        delete();
    }

    public void delete() {
        if (id != 0) {
            glDeleteTextures(id);
        }
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getComponents() {
        return components;
    }
}
