package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp boolExp(boolean b) { return new ValueExp(new BoolValue(b)); }
    private static IExp intExp(int n) { return new ValueExp(new IntValue(n)); }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void logicAndWorksCorrectly() throws MyException {
        IExp e = new LogicExp(LogicOp.AND, boolExp(true), boolExp(false));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void logicOrWorksCorrectly() throws MyException {
        IExp e = new LogicExp(LogicOp.OR, boolExp(false), boolExp(true));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void nonBooleanOperandThrowsException() {
        IExp e = new LogicExp(LogicOp.AND, boolExp(true), intExp(5));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }
}
