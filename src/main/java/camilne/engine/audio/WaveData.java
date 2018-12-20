package camilne.engine.audio;

import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;

public class WaveData {

    private final int format;
    private final int sampleRate;
    private final ByteBuffer data;

    public WaveData(String path) throws AudioException {
        var inputStream = WaveData.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException("Audio file does not exist: " + path);
        }

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream))) {

            var audioFormat = audioStream.getFormat();
            format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());

            sampleRate = (int) audioFormat.getSampleRate();
            var totalBytes = (int) (audioStream.getFrameLength() * audioFormat.getFrameSize());
            data = BufferUtils.createByteBuffer(totalBytes);

            var dataArray = new byte[totalBytes];
            var bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.put(dataArray, 0, bytesRead);
            data.flip();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new AudioException(e);
        }
    }

    public void clear() {
        data.clear();
    }

    public int getFormat() {
        return format;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public ByteBuffer getData() {
        return data;
    }

    private static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
        } else {
            return bitsPerSample == 8 ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
        }
    }

}
