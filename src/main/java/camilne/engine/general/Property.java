package camilne.engine.general;

import java.util.ArrayList;
import java.util.List;

public class Property<T> {

    private T value;
    private List<PropertyChanged<T>> observers;

    public Property(T initialValue) {
        value = initialValue;
        observers = new ArrayList<>();
    }

    public void addChangeObserver(PropertyChanged<T> propertyChanged) {
        if (!observers.contains(propertyChanged)) {
            observers.add(propertyChanged);
        }
    }

    public void removeChangeObserver(PropertyChanged<T> propertyChanged) {
        observers.remove(propertyChanged);
    }

    public void set(T value) {
        if (this.value.equals(value)) {
            return;
        }

        for (var observer : observers) {
            observer.execute(this, this.value, value);
        }
        this.value = value;
    }

    public T get() {
        return value;
    }
}
