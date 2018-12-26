package camilne.engine.graphics.gui;

import camilne.engine.graphics.Batch;
import camilne.engine.graphics.font.Font;

public class Label extends Component {

    private Font font;
    private String text;
    private Image background;

    public Label(Font font, String text) {
        setFont(font);
        setText(text);
    }

    @Override
    public void render(Batch batch) {
        background.render(batch);
        batch.draw(font, text, getX(), getY() + font.getHeight());
    }

    private void updateSize() {
        if (font != null && text != null) {
            setWidth(font.getStringWidth(text));
            setHeight(font.getStringHeight(text));
        }

        if (background != null) {
            background.setX(getX());
            background.setY(getY() + font.getHeight() - getHeight());
            background.setWidth(getWidth());
            background.setHeight(getHeight());
        }
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        updateSize();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateSize();
    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
        updateSize();
    }
}
