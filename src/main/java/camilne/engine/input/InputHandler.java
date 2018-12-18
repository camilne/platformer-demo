package camilne.engine.input;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    private static InputHandler instance;

    private Map<Integer, InputAction> keyDownActions;
    private Map<Integer, InputAction> keyUpActions;
    private long window;

    private InputHandler() {
        keyDownActions = new HashMap<>();
        keyUpActions = new HashMap<>();
    }

    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    public boolean isKeyDown(int key) {
        if (window == 0) {
            throw new IllegalStateException("Window is not set");
        }

        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public void setWindow(long window) {
        this.window = window;
        glfwSetKeyCallback(window, this::keyCallback);
    }

    public void addKeyDownAction(int key, InputAction action) {
        keyDownActions.put(key, action);
    }

    public void addKeyUpAction(int key, InputAction action) {
        keyUpActions.put(key, action);
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS && keyDownActions.containsKey(key)) {
            keyDownActions.get(key).execute();
        } else if (action == GLFW_RELEASE && keyUpActions.containsKey(key)) {
            keyUpActions.get(key).execute();
        }
    }

}
