package st.netb.mc.mcworld.datastructs.raw;

public class Tuple<T> {

    private T first;
    private T second;

    public Tuple(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T first() {
        return first;
    }

    public T second() {
        return second;
    }
}
