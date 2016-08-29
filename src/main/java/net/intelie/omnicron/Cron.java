package net.intelie.omnicron;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

public class Cron implements Field {
    private static final long serialVersionUID = 1L;
    private static final int MAX_RETRIES = 100;

    private final List<Field> fields;
    private final List<Field> evalOrder;

    public Cron(String expression) {
        this(CronParsingHelper.parse(expression));
    }

    public Cron(Field seconds, Field minutes, Field hours, Field days, Field months, Field daysOfWeek) {
        this(Arrays.asList(seconds, minutes, hours, days, months, daysOfWeek));
    }

    private Cron(List<Field> fields) {
        this.fields = fields;
        this.evalOrder = new ArrayList<>(fields);
        Collections.reverse(evalOrder);
        evalOrder.add(new ValueField(FieldType.NANOS, 0));
    }

    public <T extends Temporal & Comparable<? super T>> T next(T date) {
        if (date == null) return null;
        return nextOrSame(ChronoUnit.NANOS.addTo(date, 1));
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T nextOrSame(T date) {
        if (date == null) return null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            T result = nextSingle(date);
            if (result == null || result.equals(date))
                return result;
            date = result;
        }
        return null;
    }

    private <T extends Temporal & Comparable<? super T>> T nextSingle(T date) {
        for (Field field : evalOrder)
            date = field.nextOrSame(date);
        return date;
    }

    public <T extends Temporal & Comparable<? super T>> T prev(T date) {
        if (date == null) return null;
        return prevOrSame(ChronoUnit.NANOS.addTo(date, -1));
    }

    @Override
    public <T extends Temporal & Comparable<? super T>> T prevOrSame(T date) {
        if (date == null) return null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            T result = prevSingle(date);
            if (result == null || result.equals(date))
                return result;
            date = result;
        }
        return null;
    }

    private <T extends Temporal & Comparable<? super T>> T prevSingle(T date) {
        for (Field field : evalOrder)
            date = field.prevOrSame(date);
        return date;
    }

    @Override
    public boolean matches(Temporal date) {
        if (date == null) return false;
        for (Field field : evalOrder)
            if (!field.matches(date))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return fields.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cron that = (Cron) o;

        return Objects.equals(this.fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }
}
