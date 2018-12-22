package camilne.platformer;

import camilne.engine.Sprite;
import camilne.engine.graphics.TextureFactory;
import camilne.engine.graphics.TextureRegion;

import java.io.IOException;

public class Tile extends Sprite {

    public static final int SIZE = 55;

    private static final String TILE_TEXTURE_PATH = "tiles.png";

    private TileType type;

    public Tile(TileType type, float x, float y) throws IOException {
        super(getTileRegion(type), x, y, SIZE, SIZE);
        setCollisionGroup("ground");
        var bounds = type.getBounds().copy();
        bounds.scale(SIZE);
        bounds.translate(x, y);
        setBounds(bounds);

        this.type = type;
    }

    private static TextureRegion getTileRegion(TileType type) throws IOException {
        var texture = TextureFactory.create(TILE_TEXTURE_PATH);
        return new TextureRegion(texture, type.getX(), type.getY(), type.getWidth(), type.getHeight());
    }
}
