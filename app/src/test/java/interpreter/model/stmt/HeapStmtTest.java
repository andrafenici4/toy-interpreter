package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        // Prepare a fresh state with one reference variable (v) and one primitive int (x)
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void newAllocatesAtAddressOneAndUpdatesVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newErrorsThrowStatementException() {
        // Undeclared variable, non-reference variable, and mismatched inner type
        assertThrows(StatementException.class,
                () -> new NewStmt("missing", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(StatementException.class,
                () -> new NewStmt("x", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(StatementException.class,
                () -> new NewStmt("v", new ValueExp(new BoolValue(true))).execute(state));
    }

    @Test
    void readHeapReturnsStoredValue() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        var val = new ReadHeapExp(new VarExp("v")).eval(state.getSymTable(), state.getHeap());
        assertEquals(new IntValue(20), val);
    }

    @Test
    void readHeapErrorsThrowExpressionException() {
        // Non-reference expression and null address (0) not present in the heap
        assertThrows(ExpressionException.class,
                () -> new ReadHeapExp(new VarExp("x")).eval(state.getSymTable(), state.getHeap()));
        assertThrows(ExpressionException.class,
                () -> new ReadHeapExp(new VarExp("v")).eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void writeHeapUpdatesStoredValue() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void writeHeapErrorsThrowStatementException() throws MyException {
        // Undeclared, non-reference, and unallocated address (0)
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("missing", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("x", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", new ValueExp(new IntValue(1))).execute(state));

        // Wrong value type after valid allocation
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", new ValueExp(new BoolValue(false))).execute(state));
    }
}
