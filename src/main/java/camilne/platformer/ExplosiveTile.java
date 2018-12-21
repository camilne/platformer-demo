package camilne.platformer;

import camilne.engine.GameObject;
import camilne.engine.audio.AudioPool;
import camilne.engine.audio.Sound;

import java.io.IOException;

public class ExplosiveTile extends Tile {
    private Sound explosion;
    private Sound buzzer;

    public ExplosiveTile(float x, float y) throws IOException {
        super(TileType.BOX_EXPLOSIVE, x, y);

        explosion = AudioPool.getInstance().createSound("grenade.wav");
        buzzer = AudioPool.getInstance().createSound("buzzer.wav");
        setTrigger(true);
    }

    @Override
    public void onEnter(GameObject other) {
        if (other instanceof Player) {
            getSource().play(explosion);
        }
    }

    @Override
    public void onExit(GameObject other) {
        if (other instanceof Player) {
            getSource().play(buzzer);
        }
    }
}
