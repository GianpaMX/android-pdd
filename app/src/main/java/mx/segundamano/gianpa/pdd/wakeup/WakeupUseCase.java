package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;

public class WakeupUseCase {
    private AlarmGateway alarmGateway;
    private Callback callback;

    public WakeupUseCase(AlarmGateway alarmGateway) {
        this.alarmGateway = alarmGateway;
    }

    public void timeUp(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onTimeUp();
    }
}
