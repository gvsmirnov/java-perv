package ru.gvsmirnov.perv.labs.util;

import java.util.concurrent.TimeUnit;

public class Util {


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
        double signum = Math.signum(nanos);

        nanos = Math.abs(nanos);


        TimeUnit unit = getUnit(nanos);

        long fullPart = unit.convert(nanos, TimeUnit.NANOSECONDS);

        long remainder = nanos - unit.toNanos(fullPart);
        long one = unit.toNanos(1);

        int fractionalPart = (int) (1000.0 * remainder / one);

        return (signum < 0 ? "-" : "") + fullPart + "." + fractionalPart + " " + unit;
    }

    public static void out(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

}
