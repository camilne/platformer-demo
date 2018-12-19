package camilne.engine.general;

@FunctionalInterface
public interface PropertyChanged<T> {

    void execute(Property<T> property, T oldValue, T newValue);

}
