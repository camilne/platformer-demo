package camilne.engine.graphics.gui;

import camilne.engine.graphics.Animation;
import camilne.engine.graphics.TextureRegion;

import java.util.List;

public class Image extends AtomicComponent {

    public Image(TextureRegion region) {
        super(new Animation(List.of(region), 1));
    }

}
