package io.github.gianpamx.pdd.alarm;

public interface Alarm {
    String EXTRA_TAG = "EXTRA_TAG";

    void setWakeUpTime(long wakeUpTime, String tag);

    void setActiveTimeUpListener(ActiveTimeUpListener activeTimeUpListener);

    ActiveTimeUpListener getActiveTimeUpListener();

    void cancel(String tag);

    interface ActiveTimeUpListener {
        void onTimeUp();
    }
}
