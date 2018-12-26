package camilne.engine.general;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class PropertyTest {

    @Mock
    private PropertyChanged<Integer> observer1;
    @Mock
    private PropertyChanged<Integer> observer2;

    @Test
    void doesSetInitialValue() {
        var property = new Property<>(false);
        assertFalse(property.get());

        var property2 = new Property<>(12);
        assertEquals(12, property2.get().intValue());
    }

    @Test
    void canSetValue() {
        var property = new Property<>(true);
        property.set(false);

        assertFalse(property.get());
    }

    @Test
    void doesNotifyObserversWhenPropertyChanged() {
        var property = new Property<>(15);
        property.addChangeObserver(observer1);
        property.addChangeObserver(observer2);

        property.set(8);
        verify(observer1, times(1)).execute(property, 15, 8);
        verify(observer2, times(1)).execute(property, 15, 8);
    }

    @Test
    void doesNotNotifyWhenPropertyDoesNotChangeValue() {
        var property = new Property<>(5);
        property.addChangeObserver(observer1);

        property.set(5);
        verify(observer1, times(0)).execute(any(), any(), any());
    }

    @Test
    void canRemoveObserver() {
        var property = new Property<>(15);
        property.addChangeObserver(observer1);
        property.removeChangeObserver(observer1);

        property.set(8);
        verify(observer1, times(0)).execute(any(), any(), any());
    }
}
