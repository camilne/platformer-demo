package camilne.engine.graphics;

import camilne.engine.Camera;
import camilne.engine.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Gui {

    private int width;
    private int height;
    private Camera camera;
    private List<Sprite> sprites;

    public Gui(int width, int height) {
        this.width = width;
        this.height = height;
        camera = new Camera(width, height);
        sprites = new ArrayList<>();

        var leftOffset = 15;
        var topOffset = 15;

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

        sprites.add(new Sprite(new TextureRegion(texture, 448, 180, 84, 64),
                leftOffset, height - topOffset - 128, 168, 128));
        sprites.add(new Sprite(new TextureRegion(texture, 551, 182, 32, 60),
                leftOffset + 168 + 16 * 10, height - topOffset - 4 - 120, 64, 120));

        for (var j = 0; j < 3; j++) {
            var yOffset = height - topOffset - 8 - 32 - j * 40;
            var filledAmount = (int) Math.round(Math.random() * 10);
            sprites.add(new Sprite(emptyBarLeft, leftOffset + 168, yOffset, 16, 32));
            if (filledAmount > 0) {
                sprites.add(new Sprite(filledBarsLeft[j], leftOffset + 168, yOffset + 4, 16, 24));
            }
            for (int i = 1; i < 10; i++) {
                var xOffset = leftOffset + 168 + i * 16;
                sprites.add(new Sprite(emptyBar, xOffset, yOffset, 16, 32));
                if (filledAmount > i) {
                    sprites.add(new Sprite(filledBars[j], xOffset, yOffset + 4, 16, 24));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (var sprite : sprites) {
            batch.draw(sprite);
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
