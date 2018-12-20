package camilne.engine.graphics.gui;

import camilne.engine.graphics.SpriteBatch;

public abstract class Component {

    private int x;
    private int y;
    private int width;
    private int height;

    public abstract void render(SpriteBatch batch);

    public void scale(float amount) {
        width *= amount;
        height *= amount;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
