package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.graphics.*;
import camilne.engine.physics.PhysicsWorld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

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
        var sprite = new Sprite(animation, 100, 600, 50, 50);
        sprite.setDynamic(true);
        var shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");

        var camera = new Camera(WIDTH, HEIGHT);

        var physicsWorld = new PhysicsWorld(-5);
        physicsWorld.addObject(sprite);

        var floorTexture = new TextureRegion(TextureFactory.create("floor.png"));
        var floor = new Sprite(floorTexture, 0, 50, 1000, 100);
        physicsWorld.addObject(floor);

        var trigger = new Sprite(floorTexture, 0, 300, 300, 30);
        trigger.setTrigger(true);
        physicsWorld.addObject(trigger);

        while (!glfwWindowShouldClose(window)) {
            physicsWorld.update(1f / 60);

            AnimationPool.getInstance().update();

            glClear(GL_COLOR_BUFFER_BIT);
            shader.bind();
            shader.setUniform("u_mvp", camera.getCombinedMatrix());

            spriteBatch.begin();
            spriteBatch.draw(sprite);
            spriteBatch.draw(floor);
            spriteBatch.draw(trigger);
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
