package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;

public class WakeupUseCase implements AlarmGateway.TimeUpListener {
    private AlarmGateway alarmGateway;
    private Callback callback;

    public WakeupUseCase(AlarmGateway alarmGateway) {
        this.alarmGateway = alarmGateway;
    }

    public void timeUp(Callback callback) {
        this.callback = callback;

        alarmGateway.addTimeUpListener(this);
        alarmGateway.timeUp();
    }

    @Override
    public void onTimeUp() {
        callback.onTimeUp();
    }

    public interface Callback {
        void onTimeUp();
    }
}
