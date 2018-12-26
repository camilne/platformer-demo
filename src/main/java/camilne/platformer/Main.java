package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.GLUtil;
import camilne.engine.Sprite;
import camilne.engine.audio.AudioPool;
import camilne.engine.graphics.*;
import camilne.engine.graphics.gui.GuiDebugRenderer;
import camilne.engine.input.InputHandler;
import camilne.engine.physics.PhysicsWorld;
import org.joml.Vector2f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final float AUDIO_SCALE = 1f / Tile.SIZE;

    private long window;
    private Shader shader;
    private SpriteBatch spriteBatch;
    private List<Sprite> sprites;
    private Camera camera;
    private World world;
    private Player player;
    private Gui gui;
    private GuiDebugRenderer guiDebugRenderer;
    private int ticks;

    public static void main(String[] args) throws Exception {
        var main = new Main();
        main.run();
    }

    private Main() {
    }

    private void run() throws Exception {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        loop();
        destroy();
    }

    private void init() throws IOException, WorldLoadingException {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        window = createWindow();
        glEnable(GL_DEBUG_OUTPUT);

        GLUtil.init();

        initInput();

        glClearColor(0.4f, 0.45f, 0.45f, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        GLUtil.checkError();

        PhysicsWorld.getInstance().setGravity(-18);
        AudioPool.getInstance().setScale(AUDIO_SCALE);
        AudioPool.getInstance().setMasterVolume(0f);

        shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");
        camera = new Camera(WIDTH, HEIGHT);
        spriteBatch = new SpriteBatch();
        spriteBatch.setShader(shader);

        sprites = new ArrayList<>();
        world = WorldReader.readWorld("world.xml");

        createObjects();

        gui = new Gui(WIDTH, HEIGHT);
        guiDebugRenderer = new GuiDebugRenderer(camera, gui.getStage());

        createSound();

        glfwShowWindow(window);
    }

    private long createWindow() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); // We want OpenGL 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // To make MacOS happy; should not be needed
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // We don't want the old OpenGL

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);

        var window = glfwCreateWindow(WIDTH, HEIGHT, "Game", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        try (var stack = stackPush()) {
            var width = stack.mallocInt(1);
            var height = stack.mallocInt(1);

            glfwGetWindowSize(window, width, height);

            // Get the resolution of the primary monitor
            var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode != null) {
                // Center the window
                glfwSetWindowPos(window, (vidmode.width() - width.get(0)) / 2,
                        (vidmode.height() - height.get(0)) / 2);
            }
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
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
        enemyAnimation.start();
        var enemy = new Sprite(enemyAnimation, 300, 600, 36, 46);
        enemy.setDynamic(true);
        enemy.setDx(32f);
        PhysicsWorld.getInstance().addObject(enemy, Set.of("ground"));
        sprites.add(enemy);
    }

    private void createSound() {
        var backgroundSound = AudioPool.getInstance().createSound("music.ogg");
        AudioPool.getInstance().setBackgroundMusic(backgroundSound, 0.25f);
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

            GLUtil.checkError();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void update(float delta) {
        player.update(delta);
        camera.centerOn(new Vector2f(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f));
        AudioPool.getInstance().setListenerPosition(camera.getPosition());
        AudioPool.getInstance().setListenerVelocity(new Vector2f(player.getDx(), player.getDy()));

        PhysicsWorld.getInstance().update(delta);

        final var warmupTicks = 3 * 60;
        if (ticks < warmupTicks) {
            var value = (float) (Math.sin((float) ticks / warmupTicks * Math.PI - Math.PI / 2f) + 1) / 2f;
            AudioPool.getInstance().setMasterVolume(value);
        } else if (ticks == warmupTicks) {
            AudioPool.getInstance().setMasterVolume(1f);
        }
        ticks++;
    }

    private void render() {
        AnimationPool.getInstance().update();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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

//        guiDebugRenderer.render();
    }

    private void destroy() {
        spriteBatch.destroy();
        guiDebugRenderer.destroy();
        shader.destroy();

        AudioPool.getInstance().destroy();
        TextureFactory.destroy();
        GLUtil.destroy();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        var cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }

}
