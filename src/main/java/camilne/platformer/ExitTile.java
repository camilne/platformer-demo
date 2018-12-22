package camilne.platformer;

import java.io.IOException;

public class ExitTile extends Tile {
    public ExitTile(float x, float y) throws IOException {
        super(TileType.WINDOW, x, y);

        setTrigger(true);
    }
}
