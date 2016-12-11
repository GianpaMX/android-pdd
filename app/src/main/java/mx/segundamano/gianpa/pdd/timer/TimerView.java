package mx.segundamano.gianpa.pdd.timer;

public interface TimerView {
    void showStopButton();

    void showStartButton();

    void onTick(String remainingTime);

    void ringAlarm();
}
