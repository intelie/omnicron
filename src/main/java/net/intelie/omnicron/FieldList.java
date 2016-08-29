package net.intelie.omnicron;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FieldList implements Field {
    private static final long serialVersionUID = 1L;
    private final List<Field> fields;

    public FieldList(Field... fields) {
        this(Arrays.asList(fields));
    }

    public FieldList(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T date) {
        T next = null;
        for (Field field : fields) {
            T candidate = field.nextOrSame(date);
            if (next == null || candidate != null && candidate.compareTo(next) < 0)
                next = candidate;
        }
        return next;
    }

    @Override
    public boolean matches(Temporal date) {
        for (Field field : fields) {
            if (field.matches(date))
                return true;
        }

        return false;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T prevOrSame(T date) {
        T next = null;
        for (Field field : fields) {
            T candidate = field.prevOrSame(date);
            if (next == null || candidate != null && candidate.compareTo(next) > 0)
                next = candidate;
        }
        return next;
    }

    @Override
    public String toString() {
        return fields.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldList that = (FieldList) o;

        return Objects.equals(this.fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }
}
