package mx.segundamano.gianpa.pdd.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String formatTime(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }
}
