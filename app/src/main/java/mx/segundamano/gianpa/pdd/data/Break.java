package mx.segundamano.gianpa.pdd.data;

public class Break {
    public static final int INACTIVE = 0;
    public static final int TIME_UP = 1;
    public static final int ACTIVE = 2;
    public static final int INTERRUPTED = 3;

    public int status;
    public long startTimeInMillis;
    public long endTimeInMillis;

    public long getRemainingTime(long currentTimeMillis) {
        long remainingTime = endTimeInMillis - currentTimeMillis;
        return remainingTime >= 0 ? remainingTime : 0;
    }

    public boolean shouldTimeUp(long currentTimeMillis) {
        return status == Break.ACTIVE && endTimeInMillis < currentTimeMillis;
    }
}
