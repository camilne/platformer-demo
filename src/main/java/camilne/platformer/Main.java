package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.graphics.*;
import camilne.engine.physics.PhysicsWorld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private long window;
    private PhysicsWorld physicsWorld;
    private SpriteBatch spriteBatch;
    private List<Sprite> sprites;
    private Camera camera;
    private Shader shader;

    public static void main(String[] args) throws Exception {
        var main = new Main();
        main.run();
    }

    private Main() { }

    public void run() throws IOException {
        init();
        loop();
        glfwTerminate();
    }

    private void init() throws IOException {
        glfwInit();
        window = createWindow();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glClearColor(0, 0, 0.4f, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch = new SpriteBatch();
        physicsWorld = new PhysicsWorld(-5);
        camera = new Camera(WIDTH, HEIGHT);
        shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");
        sprites = new ArrayList<>();

        createObjects();
    }

    private long createWindow() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(WIDTH, HEIGHT, "Game", NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }

    private void createObjects() throws IOException {
        var characterFrame = new TextureRegion(TextureFactory.create("characters.png"), 0, 0, 32, 32);
        var characterAnimation = new Animation(characterFrame, 4, 20);
        characterAnimation.start();
        var character = new Sprite(characterAnimation, 100, 600, 50, 50);
        character.setDynamic(true);
        character.setDx(32f);
        physicsWorld.addObject(character, Set.of("ground"));
        sprites.add(character);

        var enemyFrame = new TextureRegion(TextureFactory.create("characters.png"), 0, 32, 32, 32);
        var enemyAnimation = new Animation(enemyFrame, 4, 20);
        enemyAnimation.start();
        var enemy = new Sprite(enemyAnimation, 200, 600, 50, 50);
        enemy.setDynamic(true);
        enemy.setDx(-32f);
        physicsWorld.addObject(enemy, Set.of("ground"));
        sprites.add(enemy);

        var floorTexture = new TextureRegion(TextureFactory.create("floor.png"));
        var floor = new Sprite(floorTexture, 0, 50, 1000, 100);
        floor.setCollisionGroup("ground");
        physicsWorld.addObject(floor, new HashSet<>());
        sprites.add(floor);

        var trigger = new Sprite(floorTexture, 0, 300, 300, 30);
        trigger.setTrigger(true);
        physicsWorld.addObject(trigger, new HashSet<>());
        sprites.add(trigger);
    }

    private void loop() {
        final float fpsLimit = 1f / 60;
        double lastTime = glfwGetTime();
        double timer = lastTime;
        double nowTime;
        double deltaTime = 0;
        int frames = 0;
        int updates = 0;

        while (!glfwWindowShouldClose(window)) {
            nowTime = glfwGetTime();
            deltaTime += (nowTime - lastTime) / fpsLimit;
            lastTime = nowTime;

            while (deltaTime >= 1.0) {
                update(fpsLimit);
                updates++;
                deltaTime--;
            }

            render();
            frames++;

            if (glfwGetTime() - timer > 1.0) {
                timer++;
                System.out.println("FPS: " + frames + " UPS: " + updates);
                updates = 0;
                frames = 0;
            }

            var error = glGetError();
            if (error != 0) {
                System.out.println("OpenGL error: " + error);
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void update(float delta) {
        physicsWorld.update(delta);
    }

    private void render() {
        AnimationPool.getInstance().update();

        glClear(GL_COLOR_BUFFER_BIT);
        shader.bind();
        shader.setUniform("u_mvp", camera.getCombinedMatrix());

        spriteBatch.begin();
        for (var sprite : sprites) {
            spriteBatch.draw(sprite);
        }
        spriteBatch.end();
    }

}
