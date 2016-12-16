package mx.segundamano.gianpa.pdd.alarmgateway;

public interface AlarmGateway {
    void start(long startTimeInMillis, long duration);

    void addTickListener(TickListener listener);

    void removeTickerListener(TickListener tickListener);

    void addTimeUpListener(TimeUpListener listener);

    long getStartTimeInMillis();

    long getEndTimeInMillis();

    void resume();

    void stop();

    void timeUp();

    boolean isActive();

    long getTimeUpInMillis(long duration);

    long getRemainingTime();

    interface TickListener extends TimeUpListener {
        void onTick();

        void onError(Throwable error);
    }

    interface TimeUpListener {
        void onTimeUp();
    }
}
