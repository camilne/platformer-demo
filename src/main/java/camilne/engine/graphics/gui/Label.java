package camilne.engine.graphics.gui;

import camilne.engine.graphics.SpriteBatch;
import camilne.engine.graphics.font.Font;

public class Label extends Component {

    private Font font;
    private String text;

    public Label(Font font, String text) {
        this.font = font;
        this.text = text;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(font, text, getX(), getY() + font.getHeight());
    }
}
