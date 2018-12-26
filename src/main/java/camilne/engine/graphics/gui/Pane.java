package camilne.engine.graphics.gui;

import camilne.engine.graphics.Batch;

import java.util.ArrayList;
import java.util.List;

public class Pane extends Component {

    private List<Component> children;

    public Pane() {
        this.children = new ArrayList<>();
    }

    @Override
    public void render(Batch batch) {
        for (var child : children) {
            child.render(batch);
        }
    }

    public void addChild(Component child) {
        children.add(child);
    }
}
