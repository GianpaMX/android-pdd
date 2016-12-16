package mx.segundamano.gianpa.pdd.timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class TimeConstants {
    public static final long SECONDS = 1000;
    public static final long MINUTES = 60 * SECONDS;
    public static final long DEFAULT_POMODORO_TIME = 20 * SECONDS;

    public static long sum(long a, long b) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(a);
        calendar.add(Calendar.MILLISECOND, (int) b);
        return calendar.getTimeInMillis();
    }
}
