package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.audio.AudioPool;
import camilne.engine.graphics.*;
import camilne.engine.input.InputHandler;
import camilne.engine.physics.PhysicsWorld;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final float AUDIO_SCALE = 1f / Tile.SIZE;

    private long window;
    private SpriteBatch spriteBatch;
    private List<Sprite> sprites;
    private Camera camera;
    private World world;
    private Player player;
    private Gui gui;

    public static void main(String[] args) throws Exception {
        var main = new Main();
        main.run();
    }

    private Main() { }

    private void run() throws Exception {
        init();
        loop();
        destroy();
    }

    private void init() throws IOException, WorldLoadingException {
        glfwInit();
        window = createWindow();

        initInput();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glClearColor(0.4f, 0.45f, 0.45f, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        PhysicsWorld.getInstance().setGravity(-18);

        var shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");
        camera = new Camera(WIDTH, HEIGHT);
        spriteBatch = new SpriteBatch();
        spriteBatch.setShader(shader);

        sprites = new ArrayList<>();
        world = WorldReader.readWorld("world.xml");

        createObjects();

        gui = new Gui(WIDTH, HEIGHT);

        createSounds();
    }

    private long createWindow() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(WIDTH, HEIGHT, "Game", NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }

    private void initInput() {
        var inputHandler = InputHandler.getInstance();
        inputHandler.setWindow(window);
    }

    private void createObjects() {
        player = new Player(200, 500);
        sprites.add(player);

        var enemyFrame = new TextureRegion(TextureFactory.create("characters.png"), 6, 73, 18, 23);
        var enemyAnimationStrip = new AnimationStrip(enemyFrame, 14, 4);
        var enemyAnimation = new Animation(enemyAnimationStrip, 20);
        enemyAnimation.setFlipX(true);
        enemyAnimation.start();
        var enemy = new Sprite(enemyAnimation, 300, 600, 36, 46);
        enemy.setDynamic(true);
        enemy.setDx(-32f);
        PhysicsWorld.getInstance().addObject(enemy, Set.of("ground"));
        sprites.add(enemy);
    }

    private void createSounds() {
        var backgroundSource = AudioPool.getInstance().createSource();
        backgroundSource.setLoop(true);
        backgroundSource.setPosition(new Vector2f(10.5f, 8.5f));
        var backgroundMusic = AudioPool.getInstance().createSound("guitar_loop_mono.wav");
        backgroundSource.play(backgroundMusic);
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
        player.update(delta);
        camera.centerOn(new Vector2f(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f));
        AudioPool.getInstance().setListenerPosition(camera.getPosition().mul(AUDIO_SCALE, new Vector2f()));

        PhysicsWorld.getInstance().update(delta);
    }

    private void render() {
        AnimationPool.getInstance().update();

        glClear(GL_COLOR_BUFFER_BIT);

        spriteBatch.setCamera(camera);
        spriteBatch.begin();
        world.render(spriteBatch);
        for (var sprite : sprites) {
            spriteBatch.draw(sprite);
        }
        spriteBatch.end();

        spriteBatch.begin();
        gui.render(spriteBatch);
        spriteBatch.end();
    }

    private void destroy() {
        AudioPool.getInstance().destroy();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

}
