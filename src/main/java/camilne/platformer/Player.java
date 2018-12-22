package camilne.platformer;

import camilne.engine.Sprite;
import camilne.engine.graphics.*;
import camilne.engine.input.InputHandler;
import camilne.engine.physics.PhysicsWorld;

import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Sprite {

    private Animation idleAnimation;
    private Animation walkAnimation;
    private Animation airUpAnimation;

    private boolean leftDown;
    private boolean rightDown;

    public Player(float x, float y) {
        super(getDefaultRegion(), x, y, 30f, 46f);

        initAnimation();
        initInput();

        setDynamic(true);
        setCollisionGroup("player");
        PhysicsWorld.getInstance().addObject(this, Set.of("ground"));
    }

    private static TextureRegion getDefaultRegion() {
        return new TextureRegion(TextureFactory.create("characters.png"), 9, 42, 15, 22);
    }

    private void initAnimation() {
        var texture = TextureFactory.create("characters.png");

        idleAnimation = new Animation(List.of(new TextureRegion(texture, 9, 42, 15, 22),
                new TextureRegion(texture, 9, 42, 15, 22),
                new TextureRegion(texture, 135, 41, 17, 22)), 30);

        walkAnimation = new Animation(new AnimationStrip(new TextureRegion(texture, 9, 41, 15, 23),
                17, 4), 7);

        airUpAnimation = new Animation(List.of(new TextureRegion(texture, 168, 41, 16, 22)),
                1);

        groundedProperty().addChangeObserver((p, ov, nv) -> {
            if (nv) {
                if (leftDown || rightDown) {
                    setAnimation(walkAnimation);
                } else {
                    setAnimation(idleAnimation);
                }
            } else {
                setAnimation(airUpAnimation);
            }
        });
    }

    private void initInput() {
        var inputHandler = InputHandler.getInstance();

        inputHandler.addKeyDownAction(GLFW_KEY_A, () -> setLeftDown(true));
        inputHandler.addKeyUpAction(GLFW_KEY_A, () -> setLeftDown(false));
        inputHandler.addKeyDownAction(GLFW_KEY_D, () -> setRightDown(true));
        inputHandler.addKeyUpAction(GLFW_KEY_D, () -> setRightDown(false));
        inputHandler.addKeyDownAction(GLFW_KEY_SPACE, () -> {
            setDy(530);
        });
    }

    private void setLeftDown(boolean down) {
        leftDown = down;

        if (!rightDown && isGrounded()) {
            setAnimation(down ? walkAnimation : idleAnimation);
        }
    }

    private void setRightDown(boolean down) {
        rightDown = down;

        if (!leftDown && isGrounded()) {
            setAnimation(down ? walkAnimation : idleAnimation);
        }
    }

    public void update(float delta) {
        if (leftDown) {
            setDx(-200);
            walkAnimation.setFlipX(true);
            idleAnimation.setFlipX(true);
            airUpAnimation.setFlipX(true);
        } else if (rightDown) {
            setDx(200);
            walkAnimation.setFlipX(false);
            idleAnimation.setFlipX(false);
            airUpAnimation.setFlipX(false);
        } else {
            setDx(0);
        }
    }

}
