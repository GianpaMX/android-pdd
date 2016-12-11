package mx.segundamano.gianpa.pdd.timer;

import mx.segundamano.gianpa.pdd.timer.alarmgateway.UnableToSaveStartTime;

public interface AlarmGateway {
    void start(long startTimeInMillis);

    void setTickListener(TickListener listener);

    long getStartTimeInMillis();

    void resume();

    void stop();

    void timeUp();

    interface TickListener {
        void onTick();
        void onTimeUp();

        void onError(Throwable error);
    }
}
