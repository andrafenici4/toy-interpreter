package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void evaluatesToVariableValueWhenDefined() throws MyException {
        symTable.put("v", new IntValue(10));
        IExp e = new VarExp("v");
        assertEquals(new IntValue(10), e.eval(symTable));
    }

    @Test
    void throwsExceptionWhenVariableUndefined() {
        IExp e = new VarExp("x");
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("variable x is not defined", ex.getMessage());
    }
}
