package mx.segundamano.gianpa.pdd.timer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerPresenter implements TimerUseCase.UserActiveCallback, TimerUseCase.StopPomodoroCallback {
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
    public void onPomodoroStatusChanged(int status) {
        switch (status) {
            case POMODORO_STATUS_ACTIVE:
                view.showStopButton();
                return;
            case POMODORO_STATUS_COMPLETE:
            case POMODORO_STATUS_DISCARDED:
            case POMODORO_STATUS_INTERRUPTED:
                view.showStartButton();
                return;
            case POMODORO_STATUS_ERROR:
            default:
                view.showErrorDialog();
        }
    }

    @Override
    public void onTimeUp() {
        if (view != null) view.askComplete();
    }

    public String formatTime(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    public void onStartButtonClick() {
        timerUseCase.startPomodoro();
    }

    public void onStopButtonClick() {
        timerUseCase.stopPomodoro(this);
    }

    @Override
    public void onStopReasonsReady(String[] stopReasons) {
        if (view != null) view.askStopReasons(stopReasons);
    }

    public void onStopReasonClick(int stopReason) {
        timerUseCase.stopPomodoro(stopReason);
    }

    public void stopOnError(int errorAction) {
        timerUseCase.stopPomodoroOnError(errorAction);
    }

    /**
     * @param completeAction Defined in {@link mx.segundamano.gianpa.pdd.R.array#pomodoro_complete_actions}
     */
    public void onCompleteActionClick(int completeAction) {
        timerUseCase.complete(completeAction == 1);
    }
}
