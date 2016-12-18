package mx.segundamano.gianpa.pdd.alarm;

public interface Alarm {
    void setWakeUpTime(long wakeUpTime);

    void setActiveTimeUpListener(ActiveTimeUpListener activeTimeUpListener);

    ActiveTimeUpListener getActiveTimeUpListener();

    void cancel();

    interface ActiveTimeUpListener {
        void onTimeUp();
    }
}
