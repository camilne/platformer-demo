package camilne.platformer;

import camilne.engine.Camera;
import camilne.engine.Sprite;
import camilne.engine.graphics.SpriteBatch;
import camilne.engine.graphics.TextureFactory;
import camilne.engine.graphics.TextureRegion;
import camilne.engine.graphics.gui.Image;
import camilne.engine.graphics.gui.Pane;
import camilne.engine.graphics.gui.Stage;

import java.util.ArrayList;
import java.util.List;

public class Gui {

    private Stage stage;

    public Gui(int width, int height) {
        var root = new Pane();
        stage = new Stage(width, height, root);

        var leftOffset = 15;
        var topOffset = 15;
        var bars = 10;

        var texture = TextureFactory.create("ui.png");
        var emptyBarLeft = new TextureRegion(texture, 533, 184, 8, 16);
        var emptyBar = new TextureRegion(texture, 542, 184, 8, 16);
        var filledBarsLeft = new TextureRegion[] {
                new TextureRegion(texture, 592, 186, 8, 12),
                new TextureRegion(texture, 592, 206, 8, 12),
                new TextureRegion(texture, 592, 226, 8, 12)
        };
        var filledBars = new TextureRegion[] {
                new TextureRegion(texture, 601, 186, 8, 12),
                new TextureRegion(texture, 601, 206, 8, 12),
                new TextureRegion(texture, 601, 226, 8, 12)
        };

        var left = new Image(new TextureRegion(texture, 448, 180, 84, 64));
        left.scale(2);
        left.setX(leftOffset);
        left.setY(height - topOffset - left.getHeight());
        root.addChild(left);

        var right = new Image(new TextureRegion(texture, 551, 182, 32, 60));
        right.scale(2);
        right.setX(leftOffset + left.getWidth() + 16 * 10);
        right.setY(height - topOffset - (left.getHeight() - right.getHeight()) / 2 - right.getHeight());
        root.addChild(right);

        for (var j = 0; j < 3; j++) {
            var filledAmount = (int) Math.round(Math.random() * bars);
            var emptyImage = new Image(emptyBarLeft);
            emptyImage.scale(2);
            emptyImage.setX(leftOffset + left.getWidth());
            var yOffset = height - topOffset - 8 - emptyImage.getHeight() - j * (emptyImage.getHeight() + 8);
            emptyImage.setY(yOffset);
            root.addChild(emptyImage);
            if (filledAmount > 0) {
                var filledImage = new Image(filledBarsLeft[j]);
                filledImage.scale(2);
                filledImage.setX(leftOffset + left.getWidth());
                filledImage.setY(yOffset + (emptyImage.getHeight() - filledImage.getHeight()) / 2);
                root.addChild(filledImage);
            }
            for (var i = 1; i < bars; i++) {
                emptyImage = new Image(emptyBar);
                emptyImage.scale(2);
                var xOffset = leftOffset + left.getWidth() + i * emptyImage.getWidth();
                emptyImage.setX(xOffset);
                emptyImage.setY(yOffset);
                root.addChild(emptyImage);

                if (filledAmount > i) {
                    var filledImage = new Image(filledBars[j]);
                    filledImage.scale(2);
                    filledImage.setX(xOffset);
                    filledImage.setY(yOffset + (emptyImage.getHeight() - filledImage.getHeight()) / 2);
                    root.addChild(filledImage);
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        stage.render(batch);
    }
}
