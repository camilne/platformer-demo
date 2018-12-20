package camilne.engine.graphics.gui;

import camilne.engine.graphics.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Pane extends Component {

    private List<Component> children;

    public Pane() {
        this.children = new ArrayList<>();
    }

    @Override
    public void render(SpriteBatch batch) {
        for (var child : children) {
            child.render(batch);
        }
    }

    public void addChild(Component child) {
        children.add(child);
    }
}
