package net.intelie.omnicron;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.ValueRange;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRulesException;

import static java.time.temporal.ChronoField.*;

public enum FieldType {
    NANOS(NANO_OF_SECOND),
    SECOND(SECOND_OF_MINUTE, NANO_OF_SECOND),
    MINUTE(MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND),
    HOUR(HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND),
    DAY(DAY_OF_MONTH, HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND),
    MONTH(MONTH_OF_YEAR, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND),
    DAY_OF_WEEK(ChronoField.DAY_OF_WEEK, HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, NANO_OF_SECOND);

    private final ChronoField field;
    private final ChronoField[] clear;

    private FieldType(ChronoField field, ChronoField... clear) {
        this.field = field;
        this.clear = clear;
    }

    public ValueRange range() {
        return field.range();
    }

    public ValueRange range(Temporal date) {
        return date.range(field);
    }

    public int get(Temporal date) {
        return date.get(field);
    }

    public <T extends Temporal> T add(T date, long amount) {
        return field.getBaseUnit().addTo(date, amount);
    }

    public <T extends Temporal> T clearToMin(T date) {
        for (ChronoField clearField : clear) {
            date = clearField.adjustInto(date, date.range(clearField).getMinimum());
        }
        return date;
    }

    public <T extends Temporal> T clearToMax(T date) {
        return NANOS.add(clearToMin(add(date, 1)), -1);
    }

}
