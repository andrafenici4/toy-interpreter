package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// Recursive reference type; record generates deep equals/hashCode automatically
public record RefType(IType inner) implements IType {
    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner); // Address 0 plays the role of null
    }

    @Override
    public String toString() {
        return "Ref(" + inner + ")";
    }
}
