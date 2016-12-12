package mx.segundamano.gianpa.pdd.alarmgateway;

public interface AlarmGateway {
    void start(long startTimeInMillis);

    void addTickListener(TickListener listener);

    void addTimeUpListener(TimeUpListener listener);

    long getStartTimeInMillis();

    void resume();

    void stop();

    void timeUp();

    interface TickListener extends TimeUpListener {
        void onTick();

        void onError(Throwable error);
    }

    interface TimeUpListener {
        void onTimeUp();
    }
}
