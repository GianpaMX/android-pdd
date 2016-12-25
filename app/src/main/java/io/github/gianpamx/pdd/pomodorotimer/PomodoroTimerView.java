package io.github.gianpamx.pdd.pomodorotimer;

public interface PomodoroTimerView {
    void showStopButton();

    void showStartButton();

    void onTick(String remainingTime);

    void askStopReasons(String[] stopReasons);

    void showErrorDialog();

    void askComplete();

    void onCompleted();
}
