package camilne.platformer;

import camilne.engine.audio.AudioPool;

import java.io.IOException;

public class SoundTile extends Tile {
    public SoundTile(float x, float y) throws IOException {
        super(TileType.BOX_ITEM, x, y);

        var sound = AudioPool.getInstance().createSound("guitar_loop_mono.wav");
        getSource().setLoop(true);
        getSource().play(sound);
    }
}
