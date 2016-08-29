package net.intelie.omnicron;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTypeTest {
    @Test
    public void testRange() throws Exception {
        assertEquals(ValueRange.of(1, 28, 31), FieldType.DAY.range());
        assertEquals(ValueRange.of(0, 23), FieldType.HOUR.range());
    }

    @Test
    public void testRangeWithDate() throws Exception {
        assertEquals(ValueRange.of(1, 29), FieldType.DAY.range(Support.brt("2016-02-20 23:59:59.123")));
        assertEquals(ValueRange.of(1, 28), FieldType.DAY.range(Support.brt("2015-02-20 23:59:59.123")));
        assertEquals(ValueRange.of(1, 31), FieldType.DAY.range(Support.brt("2016-03-20 23:59:59.123")));
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(20, FieldType.DAY.get(Support.brt("2016-02-20 23:59:59.123")));
        assertEquals(2, FieldType.MONTH.get(Support.brt("2016-02-20 23:59:59.123")));
        assertEquals(6, FieldType.DAY_OF_WEEK.get(Support.brt("2016-02-20 23:59:59.123")));
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(Support.brt("2016-02-21 23:59:59.123"),
                FieldType.DAY.add(Support.brt("2016-02-20 23:59:59.123"), 1));

        assertEquals(Support.brt("2016-03-20 23:59:59.123"),
                FieldType.MONTH.add(Support.brt("2016-02-20 23:59:59.123"), 1));

        assertEquals(Support.brt("2016-02-21 23:59:59.123"),
                FieldType.DAY_OF_WEEK.add(Support.brt("2016-02-20 23:59:59.123"), 1));
    }

    @Test
    public void testClearToMin() throws Exception {
        assertEquals(Support.brt("2016-02-20 00:00:00"),
                FieldType.DAY.clearToMin(Support.brt("2016-02-20 23:59:59.123")));

        assertEquals(Support.brt("2016-02-01 00:00:00"),
                FieldType.MONTH.clearToMin(Support.brt("2016-02-20 23:59:59.123")));

        assertEquals(Support.brt("2016-02-20 00:00:00"),
                FieldType.DAY_OF_WEEK.clearToMin(Support.brt("2016-02-20 23:59:59.123")));
    }

    @Test
    public void testClearToMax() throws Exception {
        assertEquals(Support.brt("2016-02-20 23:59:59.999999999").withLaterOffsetAtOverlap(),
                FieldType.DAY.clearToMax(Support.brt("2016-02-20 23:59:59.123")));

        assertEquals(Support.brt("2016-02-29 23:59:59.999999999"),
                FieldType.MONTH.clearToMax(Support.brt("2016-02-20 23:59:59.123")));

        assertEquals(Support.brt("2016-02-20 23:59:59.999999999").withLaterOffsetAtOverlap(),
                FieldType.DAY_OF_WEEK.clearToMax(Support.brt("2016-02-20 23:59:59.123")));
    }

    @Test
    public void testClearNanos() throws Exception {
        assertEquals(Support.brt("2016-02-20 12:34:56.123456789"),
                FieldType.NANOS.clearToMin(Support.brt("2016-02-20 12:34:56.123456789")));
        assertEquals(Support.brt("2016-02-20 12:34:56.123456789"),
                FieldType.NANOS.clearToMax(Support.brt("2016-02-20 12:34:56.123456789")));
    }
}