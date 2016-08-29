package net.intelie.omnicron;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static org.junit.Assert.*;

public class Support {
    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss") // .parseLenient()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .toFormatter();

    public static final DateTimeFormatter BRT = FORMATTER.withZone(ZoneId.of("America/Sao_Paulo"));
    public static final DateTimeFormatter UTC = FORMATTER.withZone(ZoneId.of("UTC"));

    public static ZonedDateTime brt(String s) {
        return ZonedDateTime.parse(s, BRT);
    }

    public static ZonedDateTime utc(String s) {
        return ZonedDateTime.parse(s, UTC);
    }


    public static void assertBrt(Field field, String dt1, String dtNext) {
        assertDates(field, Support.brt(dt1), Support.brt(dtNext));
    }

    public static void assertDates(Field field, ZonedDateTime dt1, ZonedDateTime dtNext) {
        assertEquals(dtNext, field.nextOrSame(dt1));
    }

    public static void assertBrt(Field field, String dt1, String dtNext, String dtPrev) {
        assertDates(field, Support.brt(dt1), Support.brt(dtNext), Support.brt(dtPrev));
    }

    public static void assertDates(Field field, ZonedDateTime dt1, ZonedDateTime dtNext, ZonedDateTime dtPrev) {
        assertEquals(dtNext, field.nextOrSame(dt1));
        assertEquals(dtPrev, field.prevOrSame(dt1));

        if (dtNext != null) {
            assertTrue(field.matches(dtNext));
            assertEquals(dtNext.equals(dt1), field.matches(dt1));
        }
        if (dtPrev != null) {
            assertTrue(field.matches(dtPrev));
            assertEquals(dtPrev.equals(dt1), field.matches(dt1));
        }
    }

    public static void assertEquality(Object base, Object... others) {
        assertEquals(base, base);
        assertEquals(base.hashCode(), base.hashCode());
        for (Object other : others) {
            assertEquals(base, other);
            assertEquals(base.hashCode(), other.hashCode());
        }
    }

    public static void assertInequality(Object base, Object... others) {
        assertNotEquals(base, new Object());
        assertNotEquals(base.hashCode(), new Object().hashCode());
        for (Object other : others) {
            assertNotEquals(base, other);
            assertNotEquals(base.hashCode(), other.hashCode());
        }
    }
}
