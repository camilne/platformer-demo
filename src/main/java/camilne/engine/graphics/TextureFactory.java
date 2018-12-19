package camilne.engine.graphics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureFactory {

    private static final Map<String, Texture> textures = new HashMap<>();

    public static Texture create(String resource) {
        if (textures.containsKey(resource)) {
            return textures.get(resource);
        }

        try {
            var texture = new Texture(resource);
            textures.put(resource, texture);
            return texture;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
