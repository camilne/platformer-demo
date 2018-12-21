package camilne.engine.audio;

import org.joml.Vector2f;

import static org.lwjgl.openal.AL10.*;

public class Source {

    private final int id;
    private boolean loop;
    private float volume;

    Source() {
        id = alGenSources();
        setVolume(1f);
        setPitch(1f);
    }

    public void play(Sound sound) {
        stop();
        alSourcei(id, AL_BUFFER, sound.getId());
        resume();
    }

    public void resume() {
        alSourcePlay(id);
    }

    public void pause() {
        alSourcePause(id);
    }

    public void stop() {
        alSourceStop(id);
    }

    public void setPosition(Vector2f position) {
        alSource3f(id, AL_POSITION, position.x * AudioPool.getInstance().getScale(), position.y * AudioPool.getInstance().getScale(), 0f);
    }

    public void setVelocity(Vector2f velocity) {
        alSource3f(id, AL_VELOCITY, velocity.x * AudioPool.getInstance().getScale(), velocity.y * AudioPool.getInstance().getScale(), 0f);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float amount) {
//        System.out.println("Setting volume to " + amount * AudioPool.getInstance().getMasterVolume());
        alSourcef(id, AL_GAIN, amount * AudioPool.getInstance().getMasterVolume());
        volume = amount;
    }

    public void setPitch(float amount) {
        alSourcef(id, AL_PITCH, amount);
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        alSourcei(id, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public boolean isPlaying() {
        return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void destroy() {
        stop();
        alDeleteSources(id);
    }

}
