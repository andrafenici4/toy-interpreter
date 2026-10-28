package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatementErrorsTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
    }

    @Test
    void redeclaredVariableThrows() {
        IStmt decl1 = new VarDeclStmt("x", new IntType());
        IStmt decl2 = new VarDeclStmt("x", new BoolType());

        assertDoesNotThrow(() -> decl1.execute(state));
        assertThrows(StatementException.class, () -> decl2.execute(state));
    }

    @Test
    void typeMismatchInAssignmentThrows() {
        IStmt decl = new VarDeclStmt("x", new IntType());
        IStmt assign = new AssignStmt("x", new ValueExp(new BoolValue(true)));

        assertDoesNotThrow(() -> decl.execute(state));
        assertThrows(StatementException.class, () -> assign.execute(state));
    }

    @Test
    void assignToUndeclaredVariableThrows() {
        IStmt assign = new AssignStmt("y", new ValueExp(new IntValue(3)));
        assertThrows(StatementException.class, () -> assign.execute(state));
    }

    @Test
    void nonBooleanConditionInIfThrows() {
        IStmt decl = new VarDeclStmt("x", new IntType());
        IStmt ifStmt = new IfStmt(new VarExp("x"), new NopStmt(), new NopStmt());

        assertDoesNotThrow(() -> decl.execute(state));
        assertThrows(StatementException.class, () -> ifStmt.execute(state));
    }
}
