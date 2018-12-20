package camilne.engine.audio;

import org.joml.Vector2f;

import static org.lwjgl.openal.AL10.*;

public class Source {

    private final int id;
    private boolean loop;

    Source() {
        id = alGenSources();
        alSourcef(id, AL_GAIN, 1f);
        alSourcef(id, AL_PITCH, 1f);
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
        alSource3f(id, AL_POSITION, position.x, position.y, 0f);
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
