package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHeapTest {
    @Test
    void allocateReturnsSequentialAddressesStartingFromOne() throws ADTException {
        IHeap heap = new MyHeap();
        int a1 = heap.allocate(new IntValue(10));
        int a2 = heap.allocate(new IntValue(20));
        int a3 = heap.allocate(new IntValue(30));

        assertEquals(1, a1);
        assertEquals(2, a2);
        assertEquals(3, a3);
        assertEquals(new IntValue(20), heap.get(2));
    }

    @Test
    void getAndUpdateMissingAddressThrowADTException() {
        IHeap heap = new MyHeap();
        assertThrows(ADTException.class, () -> heap.get(1));
        assertThrows(ADTException.class, () -> heap.update(1, new IntValue(5)));
    }
}
