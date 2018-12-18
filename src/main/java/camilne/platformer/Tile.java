package camilne.platformer;

import camilne.engine.Sprite;
import camilne.engine.graphics.TextureFactory;
import camilne.engine.graphics.TextureRegion;
import camilne.engine.physics.PhysicsWorld;

import java.io.IOException;
import java.util.HashSet;

public class Tile extends Sprite {

    private static final String TILE_TEXTURE_PATH = "tiles.png";

    private TileType type;

    public Tile(TileType type, float x, float y, float size) throws IOException {
        super(getTileRegion(type), x, y, size, size);
        setCollisionGroup("ground");

        this.type = type;
        PhysicsWorld.getInstance().addObject(this, new HashSet<>());
    }

    private static TextureRegion getTileRegion(TileType type) throws IOException {
        var texture = TextureFactory.create(TILE_TEXTURE_PATH);
        return new TextureRegion(texture, type.getX(), type.getY(), type.getWidth(), type.getHeight());
    }
}
