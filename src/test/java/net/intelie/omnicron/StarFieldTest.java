package net.intelie.omnicron;

import org.junit.Test;

import static net.intelie.omnicron.Support.assertBrt;
import static org.junit.Assert.*;

public class StarFieldTest {
    @Test
    public void testSeconds() throws Exception {
        StarField field = new StarField();

        assertBrt(field, "2016-08-19 11:01:56.123", "2016-08-19 11:01:56.123", "2016-08-19 11:01:56.123");
        assertBrt(field, "2016-08-19 11:02:10.123", "2016-08-19 11:02:10.123", "2016-08-19 11:02:10.123");
        assertBrt(field, "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123", "2016-08-19 11:02:12.123");
    }

    @Test
    public void testToString() throws Exception {
        StarField field = new StarField();

        assertEquals("*", field.toString());
    }

    @Test
    public void whenAreEqual() throws Exception {
        StarField e1 = new StarField();
        StarField e2 = new StarField();

        Support.assertEquality(e1, e2);
    }

    @Test
    public void whenAreDifferent() throws Exception {
        StarField e1 = new StarField();
        ValueField e2 = new ValueField(FieldType.SECOND, 13);

        Support.assertInequality(e1, e2);
    }
}