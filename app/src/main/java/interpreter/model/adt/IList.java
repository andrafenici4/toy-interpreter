package interpreter.model.adt;

public interface IList<T> {
    void add(T v);
    T get(int index);
    int size();
}
