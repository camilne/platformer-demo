package camilne.engine.audio;

import camilne.engine.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WaveData {

    private final SoundData data;

    public WaveData(String path) throws AudioException {
        if (path.endsWith(".wav")) {
            data = readWav(path);
        } else if (path.endsWith(".ogg")) {
            data = readVorbis(path);
        } else {
            throw new AudioException("Unsupported audio type: " + path);
        }
    }

    public int getFormat() {
        return data.getFormat();
    }

    public ShortBuffer getData() {
        return data.getData();
    }

    public int getSampleRate() {
        return data.getSampleRate();
    }

    public void clear() {
        data.getData().clear();
    }

    private static SoundData readWav(String path) throws AudioException {
        var inputStream = WaveData.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException("Audio file does not exist: " + path);
        }

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream))) {

            var audioFormat = audioStream.getFormat();
            var format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());

            var sampleRate = (int) audioFormat.getSampleRate();
            var totalBytes = (int) (audioStream.getFrameLength() * audioFormat.getFrameSize());
            var data = BufferUtils.createByteBuffer(totalBytes);

            var dataArray = new byte[totalBytes];
            var bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.put(dataArray, 0, bytesRead);
            data.flip();

            return new SoundData(format, sampleRate, data.asShortBuffer());
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new AudioException(e);
        }
    }

    private static SoundData readVorbis(String path) throws AudioException {
        ByteBuffer vorbis;
        try {
            vorbis = IOUtil.ioResourceToByteBuffer(path, 32 * 1024);
        } catch (IOException e) {
            throw new AudioException(e);
        }

        var error = BufferUtils.createIntBuffer(1);
        var decoder = stb_vorbis_open_memory(vorbis, error, null);
        if (decoder == NULL) {
            throw new AudioException("Failed to open Vorbis file: " + error.get(0));
        }

        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            stb_vorbis_get_info(decoder, info);

            var channels = info.channels();
            var sampleRate = info.sample_rate();

            var data = BufferUtils.createShortBuffer(stb_vorbis_stream_length_in_samples(decoder) * channels);

            stb_vorbis_get_samples_short_interleaved(decoder, channels, data);
            stb_vorbis_close(decoder);

            return new SoundData(getOpenAlFormat(channels, 16), sampleRate, data);
        }
    }

    private static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
        } else {
            return bitsPerSample == 8 ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
        }
    }

}
