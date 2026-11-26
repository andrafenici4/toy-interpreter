package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.Map;

public interface IHeap {
    int allocate(IValue value); // Stores value at a new unique address
    IValue get(int address) throws ADTException;
    void update(int address, IValue value) throws ADTException;
    boolean isDefined(int address);
    Map<Integer, IValue> getContent(); // Read-only snapshot of the heap
    void setContent(Map<Integer, IValue> content); // Replaces content after GC
}
