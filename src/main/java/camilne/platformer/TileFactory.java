package camilne.platformer;

import java.io.IOException;

public class TileFactory {

    public static Tile create(String id, float x, float y) throws IOException {
        switch (id) {
            case "sound_tile":
                return new SoundTile(x * Tile.SIZE, y * Tile.SIZE);
            default:
                var type = TileType.valueOf(id.toUpperCase());
                return new Tile(type, x * Tile.SIZE, y * Tile.SIZE);
        }
    }

}
