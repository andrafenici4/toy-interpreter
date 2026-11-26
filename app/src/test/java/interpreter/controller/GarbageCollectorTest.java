package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GarbageCollectorTest {
    @Test
    void keepsLocationsReachableThroughHeap() {
        // Ex9 state after new(v, 30): v -> (3, int), a -> (2, Ref(int))
        List<IValue> symTable = List.of(
                new RefValue(3, new IntType()),
                new RefValue(2, new RefType(new IntType()))
        );
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(20),
                2, new RefValue(1, new IntType()),
                3, new IntValue(30),
                4, new IntValue(7)
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        assertEquals(Set.of(1, 2, 3), kept.keySet()); // 4 is garbage; 1 is reachable via 2
    }

    @Test
    void removesUnreachableChainAndIgnoresNullAddress() {
        // Lab 8 Ex2: SymTable has a -> (3, Ref(int)) and c -> (0, int)
        List<IValue> symTable = List.of(
                new RefValue(3, new RefType(new IntType())),
                new RefValue(0, new IntType())
        );
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(7),
                2, new IntValue(8),
                3, new RefValue(2, new IntType()),
                4, new RefValue(1, new IntType()),
                5, new RefValue(4, new RefType(new IntType()))
        );

        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        assertEquals(Set.of(3), GarbageCollector.unsafeGarbageCollector(roots, heap).keySet());
        assertEquals(Set.of(2, 3), GarbageCollector.safeGarbageCollector(roots, heap).keySet()); // 1, 4, 5 are garbage
    }
}
