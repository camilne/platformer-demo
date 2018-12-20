package camilne.engine.graphics.gui;

import camilne.engine.Camera;
import camilne.engine.graphics.SpriteBatch;

public class Stage {

    private Pane root;
    private Camera camera;

    public Stage(int width, int height, Pane root) {
        this.root = root;
        camera = new Camera(width, height);
    }

    public void render(SpriteBatch batch) {
        batch.setCamera(camera);
        root.render(batch);
    }

}
