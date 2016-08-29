package net.intelie.omnicron;

import java.io.Serializable;
import java.time.temporal.Temporal;

public interface Field extends Serializable {
    boolean matches(Temporal date);

    <T extends Temporal & Comparable<? super T>> T nextOrSame(T date);

    <T extends Temporal & Comparable<? super T>> T prevOrSame(T date);
}
