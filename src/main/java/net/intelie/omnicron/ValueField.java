package net.intelie.omnicron;

import java.time.temporal.Temporal;
import java.time.temporal.ValueRange;
import java.util.Objects;

public class ValueField implements Field {
    private static final long serialVersionUID = 1L;

    private final FieldType field;
    private final int value;

    public ValueField(FieldType field, int value) {
        ValueRange range = field.range();
        CronException.check(value >= range.getMinimum() && value <= range.getMaximum(),
                "Invalid value: %d (allowed %s)", value, range);

        this.field = field;
        this.value = value;
    }

    @Override
    public boolean matches(Temporal date) {
        return field.get(date) == value;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T date) {
        if (date == null) return null;
        int current = field.get(date);
        if (current == value) return date;

        while (current != value) {
            date = tryNext(date, value, current);
            current = field.get(date);
        }

        return field.clearToMin(date);
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T prevOrSame(T date) {
        if (date == null) return null;
        int current = field.get(date);
        if (current == value) return date;

        while (current != value) {
            date = tryPrevious(date, value, current);
            current = field.get(date);
        }

        return field.clearToMax(date);
    }

    private <T extends Temporal> T tryNext(T date, int value, int current) {
        if (current < value) {
            return field.add(date, value - current);
        } else {
            ValueRange range = field.range(date);
            return field.add(date, value + range.getMaximum() - current + 1 - range.getMinimum());
        }
    }

    private <T extends Temporal> T tryPrevious(T date, int value, int current) {
        if (current > value) {
            return field.add(date, value - current);
        } else {
            ValueRange range = field.range(date);;
            return field.add(date, -(current - range.getMinimum() + 1));
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueField that = (ValueField) o;

        return Objects.equals(this.field, that.field) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value);
    }
}
