package net.intelie.omnicron;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CronExceptionTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testOk() throws Exception {
        CronException.check(2 + 2 == 4, "xxx");
        CronException.check(2 + 2 == 4, "xxx", 123, 456);
    }

    @Test
    public void testFail1() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("abc 1 qwe X");

        CronException.check(2 + 2 == 5, "abc %d qwe %s", 1, "X");
    }

    @Test
    public void testFail2() throws Exception {
        exception.expect(CronException.class);
        exception.expectMessage("abc qwe");

        CronException.check(2 + 2 == 5, "abc qwe");

    }
}