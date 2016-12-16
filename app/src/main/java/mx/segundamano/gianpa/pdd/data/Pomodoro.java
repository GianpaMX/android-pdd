package mx.segundamano.gianpa.pdd.data;

public class Pomodoro {
    public long endTimeMillis;

    public long getRemainingTime(long currentTimeMillis) {
        return endTimeMillis - currentTimeMillis;
    }
}
