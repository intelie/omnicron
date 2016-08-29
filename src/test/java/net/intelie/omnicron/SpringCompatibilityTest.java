package net.intelie.omnicron;

import org.junit.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class SpringCompatibilityTest {
    @Test
    public void testCompatibility() throws Exception {
        assertCompatibility("* * * * * *");
        assertCompatibility("0 * * * * *");
        assertCompatibility("0 0 * * * *");
        assertCompatibility("0 0 0 * * *");
        assertCompatibility("0 0 0 1 * *");
        assertCompatibility("0 0 0 1 1 *");
        assertCompatibility("0 0 0 * 1 1");

        assertCompatibility("* * * * * mon-wed,sat");

        assertCompatibility("0 5 0 * * *");
        assertCompatibility("0 15 14 1 * *");
        assertCompatibility("0 0 22 * * 1-5");
        assertCompatibility("0 23 0-23/2 * * *");
        assertCompatibility("0 5 4 * * sun");
        assertCompatibility("0 0 4 8-14 * *");
        assertCompatibility("0 1,2-10/3,30-59/4 * * * *");

        assertCompatibility("0 0 0,12 1 */2 *");
        assertCompatibility("0 0 2 1-10 * *");
        //incompatible
        //assertCompatibility("0 0 4 15-21 * 1");
        assertCompatibility("0 4 8-14 * * *");

        assertCompatibility("0 30 08 10 06 *");
        assertCompatibility("0 00 11,16 * * *");
        assertCompatibility("0 00 09-18 * * *");
        assertCompatibility("0 00 09-18 * * 1-5");
        assertCompatibility("0 */10 * * * *");
    }

    private void assertCompatibility(String expression) throws ParseException {
        assertWindowTZ("America/Sao_Paulo", expression);
        assertWindowTZ("UTC", expression);
    }

    private void assertWindowTZ(String tzname, String expression) throws ParseException {
        ZoneId zone = ZoneId.of(tzname);

        Cron s1 = new Cron(expression);
        CronSequenceGenerator s2 = new CronSequenceGenerator(expression, TimeZone.getTimeZone(tzname));

        ZonedDateTime date = Support.brt("2016-02-21 13:14:00").withZoneSameLocal(zone);
        for (int i = 0; i < 100; i++) {
            long dateMillis = date.toInstant().toEpochMilli();
            ZonedDateTime next = s1.next(date);

            Date expected = s2.next(new Date(dateMillis));
            ZonedDateTime expectedNext = ZonedDateTime.ofInstant(Instant.ofEpochMilli(expected.getTime()), zone);

            assertEquals(expectedNext, next);
            date = next;
        }
    }
}
