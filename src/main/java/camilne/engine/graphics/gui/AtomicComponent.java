package camilne.engine.graphics.gui;

import camilne.engine.graphics.Animation;
import camilne.engine.graphics.SpriteBatch;

public abstract class AtomicComponent extends Component {

    private Animation animation;

    public AtomicComponent(Animation animation) {
        this.animation = animation;
        animation.start();

        setWidth(animation.getCurrentFrame().getWidth());
        setHeight(animation.getCurrentFrame().getHeight());
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(animation.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());
    }
}
