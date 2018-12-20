package camilne.engine.audio;

import java.nio.ShortBuffer;

public class SoundData {

    private final int format;
    private final int sampleRate;
    private final ShortBuffer data;

    public SoundData(int format, int sampleRate, ShortBuffer data) {
        this.format = format;
        this.sampleRate = sampleRate;
        this.data = data;
    }

    public int getFormat() {
        return format;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public ShortBuffer getData() {
        return data;
    }
}
