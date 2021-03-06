package camilne.engine;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SetupLwjglThreadExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        var window = glfwCreateWindow(1, 1, "", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @Override
    public void close() {
        glfwTerminate();
    }
}
