package net.intelie.omnicron;

import java.time.temporal.Temporal;
import java.time.temporal.ValueRange;
import java.util.Objects;

public class RangeField implements Field {
    private static final long serialVersionUID = 1L;

    private final FieldType field;
    private final int start;
    private final int end;
    private final int step;

    public RangeField(FieldType field, Integer start, Integer end, Integer step) {
        this.field = field;

        ValueRange range = field.range();

        CronException.check(start == null || start >= range.getMinimum() && start <= range.getMaximum(),
                "Invalid value: %d (allowed %s)", start, range);
        CronException.check(end == null || end >= range.getMinimum() && end <= range.getMaximum(),
                "Invalid value: %d (allowed %s)", start, range);
        CronException.check(end == null || start == null || start <= end,
                "Interval end must be >= start (%d > %d)", start, end);

        this.step = step != null ? step : 1;
        this.start = start != null ? start : (int) range.getMinimum();
        this.end = end != null ? roundDown(end) : (int) range.getMaximum();
    }

    private int roundUp(int current) {
        return roundDown(current + step - 1);
    }

    private int roundDown(int current) {
        return (current - start % step) / step * step + start % step;
    }

    @Override
    public boolean matches(Temporal date) {
        int current = field.get(date);
        return roundUp(current) == current && current >= start && current <= end;
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T date) {
        if (date == null) return null;
        if (matches(date)) return date;

        while (!matches(date)) {
            int current = field.get(date);

            ValueRange range = field.range(date);
            int min = Math.max(start, roundUp((int) range.getMinimum()));
            int max = Math.min(end, roundDown((int) range.getMaximum()));
            int proposed = roundUp(current);

            if (proposed < min) {
                date = field.add(date, min - current);
            } else if (proposed <= max) {
                date = field.add(date, proposed - current);
            } else {
                date = field.add(date, range.getMaximum() - current + 1);
            }
        }

        return field.clearToMin(date);
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T prevOrSame(T date) {
        if (date == null) return null;
        if (matches(date)) return date;

        while (!matches(date)) {
            int current = field.get(date);

            ValueRange range = field.range(date);
            int min = Math.max(start, roundUp((int) range.getMinimum()));
            int max = Math.min(end, roundDown((int) range.getMaximum()));
            int proposed = roundDown(current);

            if (proposed > max) {
                date = field.add(date, max - proposed);
            } else if (proposed >= min) {
                date = field.add(date, proposed - current);
            } else {
                date = field.add(date, range.getMinimum() - current - 1);
            }
        }

        return field.clearToMax(date);
    }


    @Override
    public String toString() {
        return start + "-" + end + (step != 1 ? "/" + step : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeField that = (RangeField) o;

        return end == that.end && start == that.start && step == that.step && field == that.field;

    }

    @Override
    public int hashCode() {
        return Objects.hash(field, start, end, step);
    }
}
