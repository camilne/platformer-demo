package camilne.platformer;

import camilne.engine.graphics.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class World {

    private List<Tile> tiles;

    public World() {
        tiles = new ArrayList<>();
    }

    public void render(SpriteBatch batch) {
        for (var tile : tiles) {
            batch.draw(tile);
        }
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

}
