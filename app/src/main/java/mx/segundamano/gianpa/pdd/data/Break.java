package mx.segundamano.gianpa.pdd.data;

public class Break {
    public static final int INACTIVE = 0;
    public static final int TIME_UP = 1;
    public static final int EXPIRED = 2;
    public static final int ACTIVE = 3;

    public int status;
    public long startTimeInMillis;
    public long endTimeInMillis;

    public long getRemainingTime(long currentTimeMillis) {
        return endTimeInMillis - currentTimeMillis;
    }
}
