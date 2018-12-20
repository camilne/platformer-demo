package camilne.platformer;

import camilne.engine.audio.AudioPool;
import org.joml.Vector2f;

import java.io.IOException;

public class SoundTile extends Tile {
    public SoundTile(float x, float y) throws IOException {
        super(TileType.BOX_ITEM, x, y);

        var sound = AudioPool.getInstance().createSound("guitar_loop_mono.wav");
        var source = AudioPool.getInstance().createSource();
        source.setPosition(new Vector2f(x / SIZE + 0.5f, y / SIZE + 0.5f));
        source.setLoop(true);
        source.play(sound);
    }
}
