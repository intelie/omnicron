package net.intelie.omnicron;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CronParsingHelper {
    private static final Map<String, Integer> MONTHS =
            aliases("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
    private static final Map<String, Integer> DAYS_OF_WEEK =
            aliases("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

    private static Map<String, Integer> aliases(String... values) {
        return Collections.unmodifiableMap(IntStream.range(0, values.length)
                .boxed().collect(Collectors.toMap(x -> values[x], x -> (x + 1))));
    }

    public static List<Field> parse(String expr) {
        String[] fields = expr.split("\\s+");
        CronException.check(fields.length == 5 || fields.length == 6,
                "Expression '%s' must define 5 or 6 fields. It has: %d.", expr, fields.length);

        int offset = fields.length - 5;
        return Arrays.asList(
                offset == 1 ? parseOther(FieldType.SECOND, fields[0]) : new ValueField(FieldType.SECOND, 0),
                parseOther(FieldType.MINUTE, fields[offset]),
                parseOther(FieldType.HOUR, fields[offset + 1]),
                parseOther(FieldType.DAY, fields[offset + 2]),
                parseMonth(fields[offset + 3]),
                parseDayOfWeek(fields[offset + 4]));
    }

    public static Field parseDayOfWeek(String field) {
        return parseField(FieldType.DAY_OF_WEEK, field, DAYS_OF_WEEK);
    }

    public static Field parseMonth(String field) {
        return parseField(FieldType.MONTH, field, MONTHS);
    }

    public static Field parseOther(FieldType type, String expr) {
        return parseField(type, expr, Collections.emptyMap());
    }

    private static Field parseField(FieldType type, String expr, Map<String, Integer> aliases) {
        try {
            List<Field> list = new ArrayList<>();
            for (String field : expr.split(","))
                list.add(parseSingle(type, field, aliases));
            if (list.size() == 1)
                return list.get(0);
            return new FieldList(list);
        } catch (CronException e) {
            throw new CronException("%s (%s = '%s')", e.getMessage(), type.toString().toLowerCase(), expr);
        }
    }

    private static Field parseSingle(FieldType type, String expr, Map<String, Integer> aliases) {
        String[] slashed = expr.split("/", 2);
        String[] dashed = slashed[0].split("-", 2);

        String start = dashed[0];
        String end = dashed.length == 2 ? dashed[1] : null;
        String step = slashed.length == 2 ? slashed[1] : null;

        if (step != null || end != null) {
            Integer startValue = parseValue(type, start, aliases);
            Integer endValue = parseValue(type, end, aliases);
            Integer stepValue = parseValue(type, step, null);

            if (startValue != null && endValue != null && startValue > endValue)
                return new FieldList(
                        new RangeField(type, null, endValue, stepValue),
                        new RangeField(type, startValue, null, stepValue));
            return new RangeField(type, startValue, endValue, stepValue);
        } else {
            Integer value = parseValue(type, start, aliases);
            if (value == null)
                return new StarField();
            return new ValueField(type, value);
        }
    }

    private static Integer parseValue(FieldType type, String expr, Map<String, Integer> aliases) {
        if (expr == null) return null;
        if ("*".equals(expr) || "".equals(expr)) return null;
        if (aliases != null) {
            Integer value = aliases.get(expr.toUpperCase());
            if (value != null) return value;
        }

        try {
            int parsed = Integer.parseInt(expr);
            if (type == FieldType.DAY_OF_WEEK && parsed == 0)
                return DayOfWeek.SUNDAY.getValue();
            return parsed;
        } catch (Exception e) {
            throw new CronException(String.format(
                    "Invalid expression: '%s'", expr));
        }
    }
}
