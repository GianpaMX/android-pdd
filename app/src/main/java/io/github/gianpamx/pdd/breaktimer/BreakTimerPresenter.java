package io.github.gianpamx.pdd.breaktimer;

import android.util.Log;

import io.github.gianpamx.pdd.utils.TimeUtils;

public class BreakTimerPresenter implements BreakTimerUseCase.UserActiveCallback {

    private static final String TAG = BreakTimerPresenter.class.getSimpleName();

    private final BreakTimerUseCase breakTimerUseCase;
    private BreakTimerView view;

    private boolean toggle;

    public BreakTimerPresenter(BreakTimerUseCase breakTimerUseCase) {
        this.breakTimerUseCase = breakTimerUseCase;
    }

    public void setView(BreakTimerView view) {
        this.view = view;
    }

    public void onActivityResume() {
        breakTimerUseCase.userActive(this);
    }

    public void onActivityPause() {
        breakTimerUseCase.userInactive();
    }

    @Override
    public void onTick(long remainingTimeInMillis, int status) {
        toggle = !toggle;
        if (view != null) {
            String timeRemaining = TimeUtils.formatTime(remainingTimeInMillis);
            Log.d(TAG, "status = " + status);
            view.onTick(status != BREAK_STATUS_TIMIE_UP ? timeRemaining : toggle ? timeRemaining : "");
        }
    }

    @Override
    public void onBreakStatusChanged(int status) {
        if (view != null && (status == BREAK_STATUS_INTERRUPTED || status == BREAK_STATUS_INACTIVE)) {
            view.onCompleted(status == BREAK_STATUS_INTERRUPTED);
        }
    }

    public void onStartButtonClick() {
        breakTimerUseCase.stopBreak(true);
    }

    public void onCancel() {
        breakTimerUseCase.stopBreak(false);
    }
}
