package ru.gvsmirnov.perv.labs.util;

import java.util.concurrent.TimeUnit;

public class Util {

    public static String shortName(TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS: return "ns";
            case MICROSECONDS: return "us";
            case MILLISECONDS: return "ms";
            case SECONDS: return "s";
            case MINUTES: return "m";
            case HOURS: return "h";
            case DAYS: return "days";
            default:return unit.toString();
        }
    }


    public static TimeUnit getUnit(long nanos) {

        for(TimeUnit unit : TimeUnit.values()) {
            long current = unit.convert(nanos, TimeUnit.NANOSECONDS);
            if(current < 1000) {
                return unit;
            }
        }

        return TimeUnit.NANOSECONDS;
    }

    public static String annotate(long nanos) {
        final boolean negative = nanos < 0;

        nanos = Math.abs(nanos);
        TimeUnit unit = getUnit(nanos);

        long fullPart = unit.convert(nanos, TimeUnit.NANOSECONDS);

        long remainder = nanos - unit.toNanos(fullPart);
        long one = unit.toNanos(1);

        int fractionalPart = (int) (1000.0 * remainder / one);

        return (negative ? "-" : "") + fullPart + "." + fractionalPart + " " + shortName(unit);
    }

    public static void out(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

}
