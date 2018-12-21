package camilne.platformer;

import camilne.engine.GameObject;
import camilne.engine.audio.AudioPool;
import camilne.engine.audio.Sound;

import java.io.IOException;

public class ExplosiveTile extends Tile {
    private Sound explosion;

    public ExplosiveTile(float x, float y) throws IOException {
        super(TileType.BOX_EXPLOSIVE, x, y);

        explosion = AudioPool.getInstance().createSound("grenade.wav");
        setTrigger(true);
    }

    @Override
    public void onCollide(GameObject other) {
        if (other instanceof Player) {
            if (!getSource().isPlaying()) {
                getSource().play(explosion);
            }
        }
    }
}
