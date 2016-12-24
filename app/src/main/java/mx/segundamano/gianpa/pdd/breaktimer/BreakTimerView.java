package mx.segundamano.gianpa.pdd.breaktimer;

public interface BreakTimerView {
    void onTick(String remainingTime);

    void onCompleted(boolean startPomodoro);

}
