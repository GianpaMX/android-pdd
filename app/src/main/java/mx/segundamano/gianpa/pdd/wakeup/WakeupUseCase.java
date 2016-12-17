package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.alarm.Alarm;

public class WakeupUseCase {
    private Alarm alarm;
    private Callback callback;

    public WakeupUseCase(Alarm alarm) {
        this.alarm = alarm;
    }

    public void timeUp(Callback callback) {
        Alarm.ActiveTimeUpListener activeTimeUpListener = alarm.getActiveTimeUpListener();
        if (activeTimeUpListener == null) {
            callback.onTimeUp();
            return;
        }

        callback.ignore();
        activeTimeUpListener.onTimeUp();
    }

    public interface Callback {
        void onTimeUp();

        void ignore();
    }
}
