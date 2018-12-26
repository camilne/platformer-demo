package camilne.engine.graphics.gui;

import camilne.engine.Camera;
import camilne.engine.graphics.Batch;
import camilne.engine.graphics.Shader;
import camilne.engine.graphics.ShapeBatch;

import java.io.IOException;

public class GuiDebugRenderer {

    private Batch batch;
    private Stage stage;

    public GuiDebugRenderer(Camera camera, Stage stage) {
        batch = new ShapeBatch();
        batch.setCamera(camera);
        try {
            var shader = new Shader("lines.vert", "lines.frag");
            shader.addUniform("u_mvp");
            batch.setShader(shader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stage = stage;
    }

    public void render() {
        batch.begin();
        stage.render(batch);
        batch.end();
    }

}
