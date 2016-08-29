package net.intelie.omnicron;

import org.junit.Test;

import static net.intelie.omnicron.Support.assertBrt;
import static org.junit.Assert.*;

public class FieldListTest {
    @Test
    public void testSeconds() throws Exception {
        Field field = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 14)
        );

        assertBrt(field, "2016-08-19 11:01:56.123", "2016-08-19 11:02:07", "2016-08-19 11:01:42.999999999");
        assertBrt(field, "2016-08-19 11:02:07.123", "2016-08-19 11:02:07.123", "2016-08-19 11:02:07.123");
        assertBrt(field, "2016-08-19 11:02:13.123", "2016-08-19 11:02:14", "2016-08-19 11:02:12.999999999");
        assertBrt(field, "2016-08-19 11:02:36.123", "2016-08-19 11:02:37", "2016-08-19 11:02:32.999999999");
        assertBrt(field, "2016-08-19 11:02:41.123", "2016-08-19 11:02:42", "2016-08-19 11:02:37.999999999");
    }
    @Test
    public void testToString() throws Exception {
        Field field = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 12)
        );

        assertEquals("7-42/5,12", field.toString());
    }

    @Test
    public void whenAreEqual() throws Exception {
        Field e1 = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 12)
        );
        Field e2 = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 12)
        );

        Support.assertEquality(e1, e2);
    }

    @Test
    public void whenAreDifferent() throws Exception {
        Field e1 = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 12)
        );
        Field e2 = new FieldList(
                new RangeField(FieldType.SECOND, 7, 49, 5),
                new ValueField(FieldType.SECOND, 12)
        );
        Field e3 = new FieldList(
                new RangeField(FieldType.SECOND, 7, 43, 5),
                new ValueField(FieldType.SECOND, 13)
        );

        Support.assertInequality(e1, e2, e3);
    }
}