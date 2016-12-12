package mx.segundamano.gianpa.pdd.timer;

import java.util.Calendar;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.alarmgateway.UnableToResume;
import mx.segundamano.gianpa.pdd.wakeup.WakeupUseCase;

public class TimerUseCase implements AlarmGateway.TickListener {
    private long pomodoroLenght = TimeConstants.DEFAULT_POMODORO_TIME;

    private AlarmGateway alarmGateway;
    private Callback callback;
    private long startTimeInMillis;

    public TimerUseCase(AlarmGateway alarmGateway) {
        this.alarmGateway = alarmGateway;
    }

    public void start(Callback callback) {
        this.callback = callback;
        startTimeInMillis = getCurrentTimeInMillis();

        alarmGateway.addTickListener(this);
        alarmGateway.start(startTimeInMillis);
    }

    private long getCurrentTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    @Override
    public void onTick() {
        callback.onTick(startTimeInMillis + pomodoroLenght - getCurrentTimeInMillis());
    }

    @Override
    public void onTimeUp() {
        callback.onTimeUp();
    }

    @Override
    public void onError(Throwable error) {
        if(error instanceof UnableToResume) {
            callback.unableToResume();
        }
    }

    public void resume(Callback callback) {
        this.callback = callback;

        alarmGateway.addTickListener(this);
        alarmGateway.resume();
        startTimeInMillis = alarmGateway.getStartTimeInMillis();
    }

    public void stop() {
        alarmGateway.stop();
    }

    public interface Callback extends WakeupUseCase.Callback {
        void onTick(long remainingTimeInMillis);

        void unableToResume();
    }
}
