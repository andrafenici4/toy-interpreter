package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.RefType;

// Stores a heap address and the type of value located at that address
public record RefValue(int address, IType locationType) implements IValue {
    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + "," + locationType + ")";
    }
}
