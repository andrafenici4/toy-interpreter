package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueExpTest {

    @Test
    void evaluatesToItsValue() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IExp e = new ValueExp(new IntValue(42));

        assertEquals(new IntValue(42), e.eval(symTable));
    }
}
