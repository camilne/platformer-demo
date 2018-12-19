package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.graphics.*;
import camilne.engine.input.InputHandler;
import camilne.engine.physics.PhysicsWorld;

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

    private long window;
    private SpriteBatch spriteBatch;
    private List<Sprite> sprites;
    private Camera camera;
    private Shader shader;
    private World world;

    private Sprite character;
    private boolean leftDown;
    private boolean rightDown;

    private Animation characterIdleAnimation;
    private Animation characterWalkAnimation;

    public static void main(String[] args) throws Exception {
        var main = new Main();
        main.run();
    }

    private Main() { }

    public void run() throws Exception {
        init();
        loop();
        glfwTerminate();
    }

    private void init() throws IOException, WorldLoadingException {
        glfwInit();
        window = createWindow();

        initInput();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        glClearColor(0, 0, 0.4f, 0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch = new SpriteBatch();
        PhysicsWorld.getInstance().setGravity(-18);
        camera = new Camera(WIDTH, HEIGHT);
        shader = new Shader("shader.vert", "shader.frag");
        shader.addUniform("u_mvp");
        sprites = new ArrayList<>();
        createObjects();
        world = WorldReader.readWorld("world.xml");


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

        inputHandler.addKeyDownAction(GLFW_KEY_A, () -> {
            leftDown = true;

            character.setAnimation(characterWalkAnimation);
            characterWalkAnimation.setFlipX(true);
            characterIdleAnimation.setFlipX(true);
        });
        inputHandler.addKeyUpAction(GLFW_KEY_A, () -> {
            leftDown = false;

            if (!rightDown) {
                character.setAnimation(characterIdleAnimation);
            }
        });
        inputHandler.addKeyDownAction(GLFW_KEY_D, () -> {
            rightDown = true;

            character.setAnimation(characterWalkAnimation);
            characterWalkAnimation.setFlipX(false);
            characterIdleAnimation.setFlipX(false);
        });
        inputHandler.addKeyUpAction(GLFW_KEY_D, () -> {
            rightDown = false;

            if (!leftDown) {
                character.setAnimation(characterIdleAnimation);
            }
        });
        inputHandler.addKeyDownAction(GLFW_KEY_SPACE, () -> character.setDy(500));
    }

    private void createObjects() throws IOException {
        characterIdleAnimation = new Animation(List.of(new TextureRegion(TextureFactory.create("characters.png"), 9, 42, 15, 22),
                new TextureRegion(TextureFactory.create("characters.png"), 9, 42, 15, 22),
                new TextureRegion(TextureFactory.create("characters.png"), 135, 41, 17, 22)), 30);

        var characterFrame = new TextureRegion(TextureFactory.create("characters.png"), 9, 42, 15, 22);
        var characterAnimationStrip = new AnimationStrip(characterFrame, 17, 4);
        characterWalkAnimation = new Animation(characterAnimationStrip, 7);

        character = new Sprite(characterIdleAnimation, 100, 600, 30, 44);
        character.setDynamic(true);
        PhysicsWorld.getInstance().addObject(character, Set.of("ground"));
        sprites.add(character);
//
//        var enemyFrame = new TextureRegion(TextureFactory.create("characters.png"), 6, 73, 18, 23);
//        var enemyAnimationStrip = new AnimationStrip(enemyFrame, 14, 4);
//        var enemyAnimation = new Animation(enemyAnimationStrip, 20);
//        enemyAnimation.start();
//        var enemy = new Sprite(enemyAnimation, 400, 600, 36, 46);
//        enemy.setDynamic(true);
//        enemy.setDx(-32f);
//        PhysicsWorld.getInstance().addObject(enemy, Set.of("ground"));
//        sprites.add(enemy);
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
        if (leftDown) {
            character.setDx(-100);
        } else if (rightDown) {
            character.setDx(100);
        } else {
            character.setDx(0);
        }

        PhysicsWorld.getInstance().update(delta);
    }

    private void render() {
        AnimationPool.getInstance().update();

        glClear(GL_COLOR_BUFFER_BIT);
        shader.bind();
        shader.setUniform("u_mvp", camera.getCombinedMatrix());

        spriteBatch.begin();
        world.render(spriteBatch);
        for (var sprite : sprites) {
            spriteBatch.draw(sprite);
        }
        spriteBatch.end();
    }

}
