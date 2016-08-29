package net.intelie.omnicron;

import org.junit.Test;

import static net.intelie.omnicron.Support.assertBrt;
import static org.junit.Assert.assertEquals;

public class RangeFieldTest {
    @Test
    public void testSeconds() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, 7, 44, 5);

        assertBrt(field, "2016-08-19 11:01:56.123", "2016-08-19 11:02:07", "2016-08-19 11:01:42.999999999");
        assertBrt(field, "2016-08-19 11:02:10.123", "2016-08-19 11:02:12", "2016-08-19 11:02:07.999999999");
        assertBrt(field, "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123");
        assertBrt(field, "2016-08-19 11:02:36.123", "2016-08-19 11:02:37", "2016-08-19 11:02:32.999999999");
        assertBrt(field, "2016-08-19 11:02:43.123", "2016-08-19 11:03:07", "2016-08-19 11:02:42.999999999");
    }

    @Test
    public void testPassTwo() throws Exception {
        RangeField field = new RangeField(FieldType.DAY, 30, 30, 5);

        assertBrt(field, "2015-02-19 11:01:56.123", "2015-03-30 00:00:00", "2015-01-30 23:59:59.999999999");
        assertBrt(field, "2015-01-31 11:01:56.123", "2015-03-30 00:00:00", "2015-01-30 23:59:59.999999999");
        assertBrt(field, "2015-01-29 11:01:56.123", "2015-01-30 00:00:00", "2014-12-30 23:59:59.999999999");
    }

    @Test
    public void testMaxGreater() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, 7, 59, 5);

        assertBrt(field, "2016-08-19 11:01:58.123", "2016-08-19 11:02:07", "2016-08-19 11:01:57.999999999");
        assertBrt(field, "2016-08-19 11:02:10.123", "2016-08-19 11:02:12", "2016-08-19 11:02:07.999999999");
        assertBrt(field, "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123");
        assertBrt(field, "2016-08-19 11:02:36.123", "2016-08-19 11:02:37", "2016-08-19 11:02:32.999999999");
        assertBrt(field, "2016-08-19 11:02:41.123", "2016-08-19 11:02:42", "2016-08-19 11:02:37.999999999");
    }

    @Test
    public void testMinLesser() throws Exception {
        RangeField field = new RangeField(FieldType.MONTH, 1, 11, null);

        assertBrt(field, "2016-12-19 11:01:56.123", "2017-01-01 00:00:00", "2016-11-30 23:59:59.999999999");
    }

    @Test
    public void testSingleValue() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, 23, 23, null);

        assertBrt(field, "2016-08-19 11:01:56.123", "2016-08-19 11:02:23", "2016-08-19 11:01:23.999999999");
        assertBrt(field, "2016-08-19 11:01:23.123", "2016-08-19 11:01:23.123", "2016-08-19 11:01:23.123");
        assertBrt(field, "2016-08-19 11:01:24.123", "2016-08-19 11:02:23", "2016-08-19 11:01:23.999999999");
    }

    @Test
    public void testToString() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, 7, 43, 5);

        assertEquals("7-42/5", field.toString());
    }

    @Test
    public void testToString2() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, 7, 43, null);

        assertEquals("7-43", field.toString());
    }

    @Test
    public void testToString3() throws Exception {
        RangeField field = new RangeField(FieldType.SECOND, null, null, null);

        assertEquals("0-59", field.toString());
    }

    @Test
    public void whenAreEqual() throws Exception {
        RangeField e1 = new RangeField(FieldType.SECOND, 7, 43, 5);
        RangeField e2 = new RangeField(FieldType.SECOND, 7, 43, 5);

        Support.assertEquality(e1, e2);
    }

    @Test
    public void whenAreDifferent() throws Exception {
        RangeField e1 = new RangeField(FieldType.SECOND, 7, 43, 5);
        RangeField e2 = new RangeField(FieldType.SECOND, 42, 43, 5);
        RangeField e3 = new RangeField(FieldType.SECOND, 7, 49, 5);
        RangeField e4 = new RangeField(FieldType.SECOND, 7, 43, 42);

        Support.assertInequality(e1, e2, e3, e4);
    }

}