package mx.segundamano.gianpa.pdd.timer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.segundamano.gianpa.pdd.notify.NotifyUseCase;

public class TimerPresenter implements TimerUseCase.Callback {
    private TimerUseCase timerUseCase;
    private TimerView view;

    public TimerPresenter(TimerUseCase timerUseCase) {
        this.timerUseCase = timerUseCase;
    }

    public void onStartButtonClick() {
        timerUseCase.start(this);
        if (view != null) view.showStopButton();
    }

    @Override
    public void onTimeUp() {
        if (view != null) {
            view.ringAlarm();
            view.showStartButton();
        }
    }

    @Override
    public void onTick(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        String remainingTime = formatter.format(date);

        if (view != null) view.onTick(remainingTime);
    }

    @Override
    public void unableToResume() {
        if (view != null) view.showStartButton();
    }

    public void setView(TimerView view) {
        this.view = view;
    }

    public void resume() {
        if (view != null) view.showStopButton();
        timerUseCase.resume(this);
    }

    public void onStopButtonClick() {
        timerUseCase.stop();
        if (view != null) view.showStartButton();
    }
}
