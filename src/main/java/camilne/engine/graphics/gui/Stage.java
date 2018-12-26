package camilne.engine.graphics.gui;

import camilne.engine.Camera;
import camilne.engine.graphics.Batch;

public class Stage {

    private Pane root;
    private Camera camera;

    public Stage(int width, int height, Pane root) {
        this.root = root;
        camera = new Camera(width, height);
    }

    public void render(Batch batch) {
        batch.setCamera(camera);
        root.render(batch);
    }

}
