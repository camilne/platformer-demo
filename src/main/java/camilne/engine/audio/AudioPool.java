package camilne.engine.audio;

import camilne.engine.general.Property;
import org.joml.Vector2f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AudioPool {

    private static AudioPool instance;

    private long device;
    private long context;
    private Map<String, Sound> sounds;
    private List<Source> sources;
    private float scale;
    private Property<Float> masterVolume;
    private Source backgroundMusicSource;

    public static synchronized AudioPool getInstance() {
        if (instance == null) {
            instance = new AudioPool();
        }
        return instance;
    }

    private AudioPool() {
        sounds = new HashMap<>();
        sources = new ArrayList<>();
        masterVolume = new Property<>(1f);
        masterVolume.addChangeObserver((p, ov, value) -> {
            for (var source : sources) {
                source.setVolume(source.getVolume());
            }
        });

        device = alcOpenDevice((ByteBuffer)null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open default audio device");
        }

        var deviceCapabilities = ALC.createCapabilities(device);
        if (!deviceCapabilities.OpenALC10) {
            throw new IllegalStateException("Device does not support OpenAL");
        }

        System.out.println("OpenALC10: " + deviceCapabilities.OpenALC10);
        System.out.println("OpenALC11: " + deviceCapabilities.OpenALC11);
        System.out.println("caps.ALC_EXT_EFX = " + deviceCapabilities.ALC_EXT_EFX);

        if (deviceCapabilities.OpenALC11) {
            var devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
            if (devices == null) {
                checkAlError();
            } else {
                for (int i = 0; i < devices.size(); i++) {
                    System.out.println(i + ": " + devices.get(i));
                }
            }
        }

        var defaultDeviceSpecifier = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
        System.out.println("Default device: " + defaultDeviceSpecifier);

        context = alcCreateContext(device, (IntBuffer) null);
        alcSetThreadContext(context);
        AL.createCapabilities(deviceCapabilities);

        System.out.println("ALC_FREQUENCY: " + alcGetInteger(device, ALC_FREQUENCY) + "Hz");
        System.out.println("ALC_REFRESH: " + alcGetInteger(device, ALC_REFRESH) + "Hz");
        System.out.println("ALC_SYNC: " + (alcGetInteger(device, ALC_SYNC) == ALC_TRUE));
        System.out.println("ALC_MONO_SOURCES: " + alcGetInteger(device, ALC_MONO_SOURCES));
        System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(device, ALC_STEREO_SOURCES));

        checkAlError();
    }

    public Sound createSound(String path) {
        if (sounds.containsKey(path)) {
            return sounds.get(path);
        }

        try {
            var id = alGenBuffers();
            var file = new WaveData(path);
            alBufferData(id, file.getFormat(), file.getData(), file.getSampleRate());
            file.clear();
            var sound = new Sound(id);
            sounds.put(path, sound);
            return sound;
        } catch (AudioException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Source createSource() {
        var source = new Source();
        sources.add(source);
        return source;
    }

    public void setListenerPosition(Vector2f position) {
        alListener3f(AL_POSITION, position.x * scale, position.y * scale, 1f);
        if (backgroundMusicSource != null) {
            backgroundMusicSource.setPosition(position);
        }
    }

    public void setListenerVelocity(Vector2f velocity) {
        alListener3f(AL_VELOCITY, velocity.x * scale, velocity.y * scale, 0f);
        if (backgroundMusicSource != null) {
            backgroundMusicSource.setVelocity(velocity);
        }
    }

    public void setBackgroundMusic(Sound sound, float volume) {
        if (backgroundMusicSource == null) {
            backgroundMusicSource = createSource();
            backgroundMusicSource.setLoop(true);
        }
        backgroundMusicSource.setVolume(volume);
        backgroundMusicSource.play(sound);
    }

    public void destroy() {
        for (var sound : sounds.values()) {
            alSourceStop(sound.getId());
            alDeleteBuffers(sound.getId());
        }
        for (var source : sources) {
            source.destroy();
        }
        alcMakeContextCurrent(NULL);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public void checkAlError() {
        var error = alcGetError(device);
        if (error != ALC_NO_ERROR) {
            throw new RuntimeException(alcGetString(device, error));
        }
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getMasterVolume() {
        return masterVolume.get();
    }

    public void setMasterVolume(float amount) {
        this.masterVolume.set(amount);
    }
}
