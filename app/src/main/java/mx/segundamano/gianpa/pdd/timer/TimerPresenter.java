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
        if (view != null) view.onTick(formatTime(remainingTimeInMillis));
    }

    public String formatTime(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    @Override
    public void unableToResume() {
        if (view != null) view.showStartButton();
    }

    @Override
    public void onPause(long remainingTimeInMillis) {
        if (view != null) {
            view.onTick(formatTime(remainingTimeInMillis));
            view.askStopReasons();
        }
    }

    public void setView(TimerView view) {
        this.view = view;

        if(view != null) {
            view.onStopReasonsReady(TimerUseCase.STOP_REASONS);
        }
    }

    public void resume() {
        if (view != null) view.showStopButton();
        timerUseCase.resume(this);
    }

    public void onPauseButtonClick() {
        timerUseCase.pause();
        if (view != null) view.showStartButton();
    }

    public void stop(int stopReason) {
        timerUseCase.stop(stopReason);
    }

    public void unpause() {
        timerUseCase.unpause();
    }
}
