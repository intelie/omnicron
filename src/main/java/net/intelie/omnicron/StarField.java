package net.intelie.omnicron;

import java.time.temporal.Temporal;

public class StarField implements Field {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean matches(Temporal date) {
        return true;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T date) {
        return date;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T prevOrSame(T date) {
        return date;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return -1520988807;
    }
}
