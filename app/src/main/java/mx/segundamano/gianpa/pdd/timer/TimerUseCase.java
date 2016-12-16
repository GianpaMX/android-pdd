package mx.segundamano.gianpa.pdd.timer;

import java.util.Calendar;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.alarmgateway.UnableToResume;
import mx.segundamano.gianpa.pdd.wakeup.WakeupUseCase;

public class TimerUseCase implements AlarmGateway.TickListener {
    private long pomodoroLenght = TimeConstants.DEFAULT_POMODORO_TIME;
    public static final String[] STOP_REASONS = {"Boss interrupted", "Colleage interrupted", "Email", "Phone call", "Web browsing"};

    private AlarmGateway alarmGateway;
    private Callback callback;
    private long startTimeInMillis;
    private long remainingTime;

    public TimerUseCase(AlarmGateway alarmGateway) {
        this.alarmGateway = alarmGateway;
    }

    public void start(Callback callback) {
        this.callback = callback;
        startTimeInMillis = getCurrentTimeInMillis();

        alarmGateway.addTickListener(this);
        alarmGateway.start(startTimeInMillis, TimeConstants.DEFAULT_POMODORO_TIME);
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
        if (error instanceof UnableToResume) {
            callback.unableToResume();
            callback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
        }
    }

    public void resume(Callback callback) {
        this.callback = callback;

        if (alarmGateway.isActive()) {
            alarmGateway.addTickListener(this);
            alarmGateway.resume();
            startTimeInMillis = alarmGateway.getStartTimeInMillis();
        } else {
            callback.unableToResume();
            callback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
        }
    }

    public void stop(int stopReason) {
        alarmGateway.stop();
        alarmGateway.removeTickerListener(this);
        callback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
    }

    public void pause() {
        remainingTime = alarmGateway.getRemainingTime();
        alarmGateway.stop();

        callback.onPause(remainingTime);
    }

    public void unpause() {
        startTimeInMillis = getCurrentTimeInMillis();

        alarmGateway.addTickListener(this);
        alarmGateway.start(startTimeInMillis, remainingTime);
    }

    public interface Callback extends WakeupUseCase.Callback {
        void onTick(long remainingTimeInMillis);

        void unableToResume();

        void onPause(long remainingTimeInMillis);
    }
}
