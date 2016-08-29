package net.intelie.omnicron;

public class CronException extends RuntimeException {
    public CronException(Object message) {
        super(String.valueOf(message));
    }

    public CronException(String message, Object... objs) {
        super(String.format(message, objs));
    }


    public static void check(boolean condition, Object errorMessage) {
        if (!condition)
            throw new CronException(errorMessage);
    }

    public static void check(boolean condition, String errorMessage, Object... objs) {
        if (!condition)
            throw new CronException(errorMessage, objs);
    }
}
