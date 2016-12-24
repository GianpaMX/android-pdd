package mx.segundamano.gianpa.pdd.pomodorotimer;

import mx.segundamano.gianpa.pdd.utils.TimeUtils;

public class PomodoroTimerPresenter implements PomodoroTimerUseCase.UserActiveCallback, PomodoroTimerUseCase.StopPomodoroCallback {
    private static final String TAG = PomodoroTimerPresenter.class.getName();

    private PomodoroTimerUseCase pomodoroTimerUseCase;
    private PomodoroTimerView view;

    public PomodoroTimerPresenter(PomodoroTimerUseCase pomodoroTimerUseCase) {
        this.pomodoroTimerUseCase = pomodoroTimerUseCase;
    }

    public void setView(PomodoroTimerView view) {
        this.view = view;
    }

    public void onActivityResume() {
        pomodoroTimerUseCase.userActive(this);
    }

    public void onActivityPause() {
        pomodoroTimerUseCase.userInactive();
    }

    @Override
    public void onTick(long remainingTime) {
        view.onTick(TimeUtils.formatTime(remainingTime));
    }

    @Override
    public void onPomodoroStatusChanged(int status) {
        switch (status) {
            case POMODORO_STATUS_ACTIVE:
                view.showStopButton();
                return;
            case POMODORO_STATUS_COMPLETE:
                view.onCompleted();
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

    public void onStartButtonClick() {
        pomodoroTimerUseCase.startPomodoro();
    }

    public void onStopButtonClick() {
        pomodoroTimerUseCase.stopPomodoro(this);
    }

    @Override
    public void onStopReasonsReady(String[] stopReasons) {
        if (view != null) view.askStopReasons(stopReasons);
    }

    public void onStopReasonClick(int stopReason) {
        pomodoroTimerUseCase.stopPomodoro(stopReason);
    }

    public void stopOnError(int errorAction) {
        pomodoroTimerUseCase.stopPomodoroOnError(errorAction);
    }

    /**
     * @param completeAction Defined in {@link mx.segundamano.gianpa.pdd.R.array#pomodoro_complete_actions}
     */
    public void onCompleteActionClick(int completeAction) {
        pomodoroTimerUseCase.complete(completeAction == 0);
    }
}
