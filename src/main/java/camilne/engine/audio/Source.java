package camilne.engine.audio;

import static org.lwjgl.openal.AL10.*;

public class Source {

    private final int id;
    private boolean loop;

    Source() {
        id = alGenSources();
        alSourcef(id, AL_GAIN, 1f);
        alSourcef(id, AL_PITCH, 1f);
        alSource3f(id, AL_POSITION, 0f, 0f, 0f);
    }

    public void play(Sound sound) {
        alSourcei(id, AL_BUFFER, sound.getId());
        alSourcePlay(id);
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        alSourcei(id, AL_LOOPING, loop ? 1 : 0);
    }

    public void destroy() {
        alDeleteSources(id);
    }

}
