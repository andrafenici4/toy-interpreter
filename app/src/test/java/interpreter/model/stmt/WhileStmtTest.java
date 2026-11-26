package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {
    // Helper that drains ExeStack without needing a Repository/Controller
    private PrgState runAll(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        while (!state.getExeStack().isEmpty()) {
            IStmt crt = state.getExeStack().pop();
            crt.execute(state);
        }
        return state;
    }

    @Test
    void whileRunsZeroTimes() throws MyException {
        // v defaults to 0, so (v > 0) is false from the start
        IStmt prog = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new WhileStmt(
                        new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        PrgState state = runAll(prog);
        assertEquals(List.of(), state.getOut().toList());
    }

    @Test
    void whileRunsOnce() throws MyException {
        IStmt prog = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(1))),
                        new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                                new VarExp("v"), new ValueExp(new IntValue(1)))))
                        )));
        PrgState state = runAll(prog);
        assertEquals(List.of(new IntValue(1)), state.getOut().toList());
    }

    @Test
    void whileRunsMultipleTimes() throws MyException {
        IStmt prog = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(3))),
                        new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                                new VarExp("v"), new ValueExp(new IntValue(1)))))
                        )));
        PrgState state = runAll(prog);
        assertEquals(List.of(new IntValue(3), new IntValue(2), new IntValue(1)), state.getOut().toList());
    }

    @Test
    void whileWithNonBooleanConditionThrows() {
        PrgState state = PrgState.of(new NopStmt());
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }
}
