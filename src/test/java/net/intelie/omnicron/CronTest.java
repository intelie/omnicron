package net.intelie.omnicron;

import org.junit.Test;

import java.time.Instant;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class CronTest {
    @Test
    public void testInvalidDate() throws Exception {
        Cron cron = new Cron("* * * 30 2 *");
        ZonedDateTime date = Support.brt("2016-10-15 13:14:00");

        assertEquals(null, cron.next(date));
        assertEquals(null, cron.prev(date));
        assertEquals(false, cron.matches(Support.brt("2016-02-30 13:14:00")));
    }

    @Test
    public void testAliases() throws Exception {
        assertAlias("@yearly", "0 0 1 1 *");
        assertAlias("@annually", "0 0 1 1 *");
        assertAlias("@monthly", "0 0 1 * *");
        assertAlias("@weekly", "0 0 * * 0");
        assertAlias("@daily", "0 0 * * *");
        assertAlias("@midnight", "0 0 * * *");
        assertAlias("@hourly", "0 * * * *");
    }

    private void assertAlias(String alias, String cron) {
        Cron cronAlias = new Cron(alias);
        Cron cronCron = new Cron(cron);
        ZonedDateTime date1 = Support.brt("2016-10-15 13:14:00");
        ZonedDateTime date2 = Support.brt("2016-10-15 13:14:00");

        for (int i = 0; i < 1000; i++) {
            date1 = cronAlias.next(date1);
            date2 = cronCron.next(date2);

            assertEquals(true, cronAlias.matches(date2));
            assertEquals(true, cronCron.matches(date1));
            assertEquals(date2, date1);
        }
    }

    @Test
    public void testNulldate() throws Exception {
        Cron cron = new Cron("* * * 30 2 *");
        assertEquals(null, cron.next(null));
        assertEquals(null, cron.prev(null));
        assertEquals(false, cron.matches(null));
    }

    @Test
    public void testDSTStartMinutes() throws Exception {
        Cron cron = new Cron("* * * * *");
        ZonedDateTime date = Support.brt("2016-10-15 13:14:00");
        long start = date.toInstant().toEpochMilli();

        for (int i = 0; i < 1000; i++) {
            date = cron.next(date);
            start += 60000;

            assertEquals(true, cron.matches(date));
            assertEquals(start, date.toInstant().toEpochMilli());
        }
    }

    @Test
    public void testDSTEndMinutes() throws Exception {
        Cron cron = new Cron("* * * * *");
        ZonedDateTime date = Support.brt("2016-02-20 13:14:00");
        long start = date.toInstant().toEpochMilli();

        for (int i = 0; i < 1000; i++) {
            date = cron.next(date);
            start += 60000;

            assertEquals(true, cron.matches(date));
            assertEquals(start, date.toInstant().toEpochMilli());
        }
    }

    @Test
    public void testDSTStartMinutesBack() throws Exception {
        Cron cron = new Cron("* * * * *");
        ZonedDateTime date = Support.brt("2016-10-15 13:14:00");
        long start = date.toInstant().toEpochMilli();

        for (int i = 0; i < 10000; i++) {
            date = cron.prev(date);
            start -= 60000;

            assertEquals(true, cron.matches(date));
            assertEquals(start, date.toInstant().toEpochMilli());
        }
    }

    @Test
    public void testDSTEndMinutesBack() throws Exception {
        Cron cron = new Cron("* * * * *");
        ZonedDateTime date = Support.brt("2016-02-20 13:14:00");
        long start = date.toInstant().toEpochMilli();

        for (int i = 0; i < 10000; i++) {
            date = cron.prev(date);
            start -= 60000;

            assertEquals(true, cron.matches(date));
            assertEquals(start, date.toInstant().toEpochMilli());
        }
    }

    @Test
    public void testWhenAreEqual() throws Exception {
        Cron e1 = new Cron("20-40/2,35 16,19 * */3 *");
        Cron e2 = new Cron("20-40/2,35 16,19 * */3 *");

        Support.assertEquality(e1, e2);
    }

    @Test
    public void testWhenAreDifferent() throws Exception {
        Cron e1 = new Cron("20-40/2,35 16,19 * */3 *");
        Cron e2 = new Cron("20-40/2,34 16,19 * */3 *");
        Cron e3 = new Cron("20-40/2,35 16,19 * */4 *");

        Support.assertInequality(e1, e2, e3);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("0 20-40/2,35 16,19 * 1-12/3 *",
                new Cron("20-40/2,35 16,19 * */3 *").toString());

        assertEquals("56 20-40/2,35 16,19 * 1-12/3 2",
                new Cron("56 20-40/2,35 16,19 * */3 Tue").toString());
    }
}