package camilne.platformer;

import camilne.engine.Shader;
import camilne.engine.Sprite;
import camilne.engine.SpriteBatch;
import camilne.engine.TextureFactory;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    public static void main(String[] args) throws Exception {
        glfwInit();
        long window = createWindow();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glClearColor(0, 0, 0.4f, 0);

        var spriteBatch = new SpriteBatch();
        var sprite = new Sprite(TextureFactory.create("sprite.png"), 0.0f, 0.0f, 1.0f, 1.0f);
        var shader = new Shader("shader.vert", "shader.frag");

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            shader.bind();

            spriteBatch.begin();
            spriteBatch.draw(sprite);
            spriteBatch.end();

            var error = glGetError();
            if (error != 0) {
                System.out.println("OpenGL error: " + error);
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();
    }

    private static long createWindow() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(1280, 720, "Game", NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }

}
