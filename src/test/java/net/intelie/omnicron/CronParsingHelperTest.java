package net.intelie.omnicron;

import com.sun.xml.internal.ws.client.sei.ValueSetterFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class CronParsingHelperTest extends CronParsingHelper {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testWrongNumberOfFields() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("Expression '* * * *' must define 5 or 6 fields. It has: 4.");

        new Cron("* * * *");
    }

    @Test
    public void testInvalidFieldValue() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("Invalid expression: 'invalid' (day_of_week = 'invalid')");

        new Cron("* * * * invalid");
    }

    @Test
    public void testInvalidFieldValue2() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("Invalid value: 8 (allowed 1 - 7) (day_of_week = '8')");

        new Cron("* * * * 8");
    }

    @Test
    public void testInvalidFieldValueAfterSlash() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("Invalid expression: 'inv/alid' (day_of_week = '1/inv/alid')");

        new Cron("* * * * 1/inv/alid");
    }

    @Test
    public void testInvalidFieldValueAfterDash() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("Invalid expression: 'inv-alid' (day_of_week = '1-inv-alid/inv/alid')");

        new Cron("* * * * 1-inv-alid/inv/alid");
    }

    @Test
    public void testAll6() throws Exception {
        assertEquals(new Cron(
                new StarField(),
                new StarField(),
                new StarField(),
                new StarField(),
                new StarField(),
                new StarField()
        ), new Cron("* * * * * *"));
    }

    @Test
    public void testMonthAliases() throws Exception {
        assertEquals(
                new FieldList(
                        new RangeField(FieldType.DAY_OF_WEEK, 2, 4, null),
                        new ValueField(FieldType.DAY_OF_WEEK, 6),
                        new ValueField(FieldType.DAY_OF_WEEK, 7)
                ),
                CronParsingHelper.parseDayOfWeek("Tue-Thu,Sat,Sun"));

        assertEquals(new ValueField(FieldType.MONTH, 1), CronParsingHelper.parseMonth("Jan"));
        assertEquals(new ValueField(FieldType.MONTH, 2), CronParsingHelper.parseMonth("Feb"));
        assertEquals(new ValueField(FieldType.MONTH, 3), CronParsingHelper.parseMonth("mar"));
        assertEquals(new ValueField(FieldType.MONTH, 4), CronParsingHelper.parseMonth("APR"));
        assertEquals(new ValueField(FieldType.MONTH, 5), CronParsingHelper.parseMonth("May"));
        assertEquals(new ValueField(FieldType.MONTH, 6), CronParsingHelper.parseMonth("JUn"));
        assertEquals(new ValueField(FieldType.MONTH, 7), CronParsingHelper.parseMonth("JUL"));
        assertEquals(new ValueField(FieldType.MONTH, 8), CronParsingHelper.parseMonth("AuG"));
        assertEquals(new ValueField(FieldType.MONTH, 9), CronParsingHelper.parseMonth("SEP"));
        assertEquals(new ValueField(FieldType.MONTH, 10), CronParsingHelper.parseMonth("oct"));
        assertEquals(new ValueField(FieldType.MONTH, 11), CronParsingHelper.parseMonth("nov"));
        assertEquals(new ValueField(FieldType.MONTH, 12), CronParsingHelper.parseMonth("DEC"));
    }

    @Test
    public void testDayOfWeekAliases() throws Exception {
        assertEquals(
                new RangeField(FieldType.MONTH, 2, 12, 2),
                CronParsingHelper.parseMonth("Feb-/2"));

        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 1), CronParsingHelper.parseDayOfWeek("mon"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 2), CronParsingHelper.parseDayOfWeek("tue"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 3), CronParsingHelper.parseDayOfWeek("wed"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 4), CronParsingHelper.parseDayOfWeek("THU"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 5), CronParsingHelper.parseDayOfWeek("FrI"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 6), CronParsingHelper.parseDayOfWeek("SaT"));
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 7), CronParsingHelper.parseDayOfWeek("suN"));
    }

    @Test
    public void testAlias0ToSunday() throws Exception {
        assertEquals(new ValueField(FieldType.DAY_OF_WEEK, 7), CronParsingHelper.parseDayOfWeek("0"));
    }

    @Test
    public void testInvertedRange() throws Exception {
        assertEquals(new FieldList(
                new RangeField(FieldType.MONTH, null, 4, 2),
                new RangeField(FieldType.MONTH, 8, null, 2)
        ), CronParsingHelper.parseMonth("Aug-Apr/2"));
    }

    @Test
    public void testComplexCase() throws Exception {
        assertEquals(new Cron(
                new ValueField(FieldType.SECOND, 0),
                new FieldList(
                        new RangeField(FieldType.MINUTE, 20, 40, 2),
                        new ValueField(FieldType.MINUTE, 35)
                ),
                new FieldList(
                        new ValueField(FieldType.HOUR, 16),
                        new ValueField(FieldType.HOUR, 19)
                ),
                new StarField(),
                new RangeField(FieldType.MONTH, null, null, 3),
                new StarField()
        ), new Cron("20-40/2,35 16,19 * */3 *"));
    }
}