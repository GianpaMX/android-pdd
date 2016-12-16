package mx.segundamano.gianpa.pdd.timer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerPresenter implements TimerUseCase.UserActiveCallback {
    private static final String TAG = TimerPresenter.class.getName();

    private TimerUseCase timerUseCase;
    private TimerView view;

    public TimerPresenter(TimerUseCase timerUseCase) {
        this.timerUseCase = timerUseCase;
    }

    public void setView(TimerView view) {
        this.view = view;
    }

    public void onActivityResume() {
        timerUseCase.userActive(this);
    }

    public void onActivityPause() {
        timerUseCase.userInactive();
    }

    @Override
    public void onTick(long remainingTime) {
        view.onTick(formatTime(remainingTime));
    }

    @Override
    public void onPomodoroResume() {
        view.showStopButton();
    }

    public String formatTime(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }
}
