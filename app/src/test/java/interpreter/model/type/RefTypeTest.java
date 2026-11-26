package interpreter.model.type;

import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeTest {
    @Test
    void defaultValueHasAddressZeroAndInnerType() {
        IType refInt = new RefType(new IntType());
        assertEquals(new RefValue(0, new IntType()), refInt.defaultValue());
    }

    @Test
    void nestedRefTypesCompareRecursively() {
        IType refRefInt1 = new RefType(new RefType(new IntType()));
        IType refRefInt2 = new RefType(new RefType(new IntType()));
        IType refInt = new RefType(new IntType());

        // Same nesting depth must be equal; different depths must differ
        assertEquals(refRefInt1, refRefInt2);
        assertNotEquals(refRefInt1, refInt);
    }
}
