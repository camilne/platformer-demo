package camilne.platformer;

import java.io.IOException;

public class TileFactory {

    public static Tile create(String id, float x, float y) throws IOException {
        var tileX = x * Tile.SIZE;
        var tileY = y * Tile.SIZE;
        switch (id) {
            case "sound_tile":
                return new SoundTile(tileX, tileY);
            case "explosive_tile":
                return new ExplosiveTile(tileX, tileY);
            case "exit":
                return new ExitTile(tileX, tileY);
            default:
                var type = TileType.valueOf(id.toUpperCase());
                return new Tile(type, tileX, tileY);
        }
    }

}
