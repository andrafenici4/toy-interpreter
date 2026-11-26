package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerA4Test {
    @TempDir
    Path tempDir; // Temporary folder so tests do not clutter the project with log files

    private PrgState run(IStmt program, String logName) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, tempDir.resolve(logName).toString());
        var ctrl = new Controller(repo, false);
        ctrl.allStep();
        return state;
    }

    @Test
    void ex7PrintsReferences() throws MyException {
        PrgState s = run(Examples.EX7, "log7.txt");
        assertEquals(List.of(
                new RefValue(1, new IntType()),
                new RefValue(2, new RefType(new IntType()))
        ), s.getOut().toList());
    }

    @Test
    void ex8Prints20And25() throws MyException {
        PrgState s = run(Examples.EX8, "log8.txt");
        assertEquals(List.of(new IntValue(20), new IntValue(25)), s.getOut().toList());
    }

    @Test
    void ex9Prints20And35() throws MyException {
        PrgState s = run(Examples.EX9, "log9.txt");
        assertEquals(List.of(new IntValue(20), new IntValue(35)), s.getOut().toList());
        assertEquals(new IntValue(30), s.getHeap().get(1));
    }

    @Test
    void ex10Prints20WithSafeGC() throws MyException {
        // Passes only if safeGarbageCollector preserves address 1 via address 2
        PrgState s = run(Examples.EX10, "log10.txt");
        assertEquals(List.of(new IntValue(20)), s.getOut().toList());
    }

    @Test
    void ex11CountsDownFrom4To0() throws MyException {
        PrgState s = run(Examples.EX11, "log11.txt");
        List<IValue> expected = List.of(
                new IntValue(4), new IntValue(3), new IntValue(2), new IntValue(1), new IntValue(0)
        );
        assertEquals(expected, s.getOut().toList());
    }
}
