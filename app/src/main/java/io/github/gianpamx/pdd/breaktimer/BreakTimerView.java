package io.github.gianpamx.pdd.breaktimer;

public interface BreakTimerView {
    void onTick(String remainingTime);

    void onCompleted(boolean startPomodoro);

}
