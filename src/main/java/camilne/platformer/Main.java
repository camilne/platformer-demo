package camilne.platformer;

import camilne.engine.*;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public static void main(String[] args) throws Exception {
        glfwInit();
        long window = createWindow();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glClearColor(0, 0, 0.4f, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        var spriteBatch = new SpriteBatch();
        var firstFrame = new TextureRegion(TextureFactory.create("characters.png"), 0, 0, 32, 32);
        var animation = new Animation(firstFrame, 4, 20);
        animation.start();
        var sprite = new Sprite(animation, 100, 100, 50, 50);
        var shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");

        var camera = new Camera(WIDTH, HEIGHT);

        while (!glfwWindowShouldClose(window)) {
            AnimationPool.getInstance().update();

            camera.translate(new Vector2f(-0.1f, 0));

            glClear(GL_COLOR_BUFFER_BIT);
            shader.bind();
            shader.setUniform("u_mvp", camera.getCombinedMatrix());

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
        long window = glfwCreateWindow(WIDTH, HEIGHT, "Game", NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }

}
