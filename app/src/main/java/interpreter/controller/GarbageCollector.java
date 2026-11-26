package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.*;
import java.util.stream.Collectors;

// Utility class for removing unreachable heap entries after each execution step
public final class GarbageCollector {
    private GarbageCollector() {}

    // Extracts all heap addresses directly referenced by variables in the SymTable
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    // UNSAFE: keeps only addresses directly in SymTable (breaks indirect heap references)
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // BFS traversal starting from SymTable roots to find all directly and transitively reachable addresses
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> worklist = new ArrayDeque<>();

        for (int addr : roots) {
            if (heap.containsKey(addr) && visited.add(addr)) {
                worklist.add(addr);
            }
        }

        while (!worklist.isEmpty()) {
            int current = worklist.poll();
            IValue value = heap.get(current);
            // If this heap location holds another reference, follow its target address too
            if (value instanceof RefValue ref) {
                int nextAddr = ref.address();
                if (heap.containsKey(nextAddr) && visited.add(nextAddr)) {
                    worklist.add(nextAddr);
                }
            }
        }
        return visited;
    }

    // SAFE: retains every heap entry reachable directly or through chains of references
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
