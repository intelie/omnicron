package net.intelie.omnicron;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class ValueFieldTest {
    @Test
    public void testSeconds() throws Exception {
        FieldType field = FieldType.SECOND;
        assertBrt(field, "2016-08-19 11:01:56.123", 12,
                "2016-08-19 11:02:12",
                "2016-08-19 11:01:12.999999999"
        );
        assertBrt(field, "2016-08-19 11:02:10.123", 12,
                "2016-08-19 11:02:12",
                "2016-08-19 11:01:12.999999999"
        );
        assertBrt(field, "2016-08-19 11:02:12.123", 12,
                "2016-08-19 11:02:12.123",
                "2016-08-19 11:02:12.123"
        );
    }

    @Test
    public void testPassTwo() throws Exception {
        FieldType field = FieldType.DAY;

        assertBrt(field, "2015-02-19 11:01:56.123", 31,
                "2015-03-31 00:00:00",
                "2015-01-31 23:59:59.999999999"
        );
    }


    @Test
    public void testNano() throws Exception {
        FieldType field = FieldType.NANOS;

        assertBrt(field, "2016-08-19 11:01:56.123", 56,
                "2016-08-19 11:01:57.000000056",
                "2016-08-19 11:01:56.000000056"
        );

        assertBrt(field, "2016-08-19 11:01:56.123", 123456789,
                "2016-08-19 11:01:56.123456789",
                "2016-08-19 11:01:55.123456789"
        );

        assertDates(field,
                Support.brt("2016-02-20 23:59:59.123").withEarlierOffsetAtOverlap(), 56,
                Support.brt("2016-02-20 23:00:00.000000056").withLaterOffsetAtOverlap(),
                Support.brt("2016-02-20 23:59:59.000000056").withEarlierOffsetAtOverlap()
        );

        assertDates(field,
                Support.brt("2016-10-15 23:59:59.123456789").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-10-16 01:00:00.000000010").withLaterOffsetAtOverlap(),
                Support.brt("2016-10-15 23:59:59.000000010").withEarlierOffsetAtOverlap()
        );
    }

    @Test
    public void testSecond() throws Exception {
        FieldType field = FieldType.SECOND;

        assertBrt(field, "2016-08-19 11:01:56.123", 56,
                "2016-08-19 11:01:56.123",
                "2016-08-19 11:01:56.123"
        );

        assertBrt(field, "2016-08-19 11:01:56.123", 57,
                "2016-08-19 11:01:57",
                "2016-08-19 11:00:57.999999999"
        );

        assertBrt(field, "2016-08-19 11:01:56.123", 10,
                "2016-08-19 11:02:10",
                "2016-08-19 11:01:10.999999999"
        );

        assertDates(field,
                Support.brt("2016-02-20 23:59:59.123").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-02-20 23:00:10").withLaterOffsetAtOverlap(),
                Support.brt("2016-02-20 23:59:10.999999999").withEarlierOffsetAtOverlap()
        );

        assertDates(field,
                Support.brt("2016-10-15 23:59:59.123").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-10-16 01:00:10").withLaterOffsetAtOverlap(),
                Support.brt("2016-10-15 23:59:10.999999999").withEarlierOffsetAtOverlap()
        );
    }

    @Test
    public void testMinute() throws Exception {
        FieldType field = FieldType.MINUTE;

        assertBrt(field, "2016-08-19 11:01:56", 1,
                "2016-08-19 11:01:56",
                "2016-08-19 11:01:56"
        );
        assertBrt(field, "2016-08-19 11:01:56", 10,
                "2016-08-19 11:10:00",
                "2016-08-19 10:10:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56", 0,
                "2016-08-19 12:00:00",
                "2016-08-19 11:00:59.999999999"
        );

        assertDates(field,
                Support.brt("2016-02-20 23:59:59.123").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-02-20 23:10:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-02-20 23:10:59.999999999").withEarlierOffsetAtOverlap()
        );

        assertDates(field,
                Support.brt("2016-10-15 23:59:59.123").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-10-16 01:10:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-10-15 23:10:59.999999999").withEarlierOffsetAtOverlap()
        );

    }

    @Test
    public void testHour() throws Exception {
        FieldType field = FieldType.HOUR;

        assertBrt(field, "2016-08-19 11:01:56.123", 11,
                "2016-08-19 11:01:56.123",
                "2016-08-19 11:01:56.123"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 15,
                "2016-08-19 15:00:00",
                "2016-08-18 15:59:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 2,
                "2016-08-20 02:00:00",
                "2016-08-19 02:59:59.999999999"
        );

        assertDates(field,
                Support.brt("2016-02-20 23:59:59.123").withEarlierOffsetAtOverlap(), 10,
                Support.brt("2016-02-21 10:00:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-02-20 10:59:59.999999999").withEarlierOffsetAtOverlap()
        );

        assertDates(field,
                Support.brt("2016-10-15 23:59:59.123").withEarlierOffsetAtOverlap(), 0,
                Support.brt("2016-10-17 00:00:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-10-15 00:59:59.999999999").withEarlierOffsetAtOverlap()
        );
    }

    @Test
    public void testDay() throws Exception {
        FieldType field = FieldType.DAY;

        assertBrt(field, "2016-08-19 11:01:56.123", 19,
                "2016-08-19 11:01:56.123",
                "2016-08-19 11:01:56.123"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 25,
                "2016-08-25 00:00:00",
                "2016-07-25 23:59:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 2,
                "2016-09-02 00:00:00",
                "2016-08-02 23:59:59.999999999"
        );
        assertBrt(field, "2016-09-19 11:01:56.123", 2,
                "2016-10-02 00:00:00",
                "2016-09-02 23:59:59.999999999"
        );
        assertBrt(field, "2016-02-19 11:01:56.123", 2,
                "2016-03-02 00:00:00",
                "2016-02-02 23:59:59.999999999");

        assertDates(field,
                Support.brt("2016-10-15 23:59:59.123").withEarlierOffsetAtOverlap(), 16,
                Support.brt("2016-10-16 01:00:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-09-16 23:59:59.999999999").withLaterOffsetAtOverlap()
        );

        assertDates(field,
                Support.brt("2016-03-15 23:59:59.123").withEarlierOffsetAtOverlap(), 20,
                Support.brt("2016-03-20 00:00:00").withLaterOffsetAtOverlap(),
                Support.brt("2016-02-20 23:59:59.999999999").withLaterOffsetAtOverlap()
        );

    }

    @Test
    public void testMonth() throws Exception {
        FieldType field = FieldType.MONTH;

        assertBrt(field, "2016-08-19 11:01:56.123", 8,
                "2016-08-19 11:01:56.123",
                "2016-08-19 11:01:56.123"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 10,
                "2016-10-01 00:00:00",
                "2015-10-31 23:59:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 6,
                "2017-06-01 00:00:00",
                "2016-06-30 23:59:59.999999999");
    }

    @Test
    public void testWeekDay() throws Exception {
        FieldType field = FieldType.DAY_OF_WEEK;

        assertBrt(field, "2016-08-19 11:01:56.123", 5,
                "2016-08-19 11:01:56.123",
                "2016-08-19 11:01:56.123"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 6,
                "2016-08-20 00:00:00",
                "2016-08-13 23:59:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 1,
                "2016-08-22 00:00:00",
                "2016-08-15 23:59:59.999999999"
        );
        assertBrt(field, "2016-08-19 11:01:56.123", 7,
                "2016-08-21 00:00:00",
                "2016-08-14 23:59:59.999999999"
        );
    }


    private void assertBrt(FieldType type, String dt1, int value, String dtNext, String dtPrev) {
        assertDates(type, Support.brt(dt1), value, Support.brt(dtNext), Support.brt(dtPrev));
    }

    private void assertDates(FieldType type, ZonedDateTime dt1, int value, ZonedDateTime dtNext, ZonedDateTime dtPrev) {
        Support.assertDates(new ValueField(type, value), dt1, dtNext, dtPrev);
    }

    @Test
    public void testToString() throws Exception {
        ValueField field = new ValueField(FieldType.SECOND, 12);

        assertEquals("12", field.toString());
    }

    @Test
    public void whenAreEqual() throws Exception {
        ValueField e1 = new ValueField(FieldType.SECOND, 12);
        ValueField e2 = new ValueField(FieldType.SECOND, 12);

        Support.assertEquality(e1, e2);
    }

    @Test
    public void whenAreDifferent() throws Exception {
        ValueField e1 = new ValueField(FieldType.SECOND, 12);
        ValueField e2 = new ValueField(FieldType.SECOND, 13);
        ValueField e3 = new ValueField(FieldType.MINUTE, 12);

        Support.assertInequality(e1, e2, e3);
    }
}