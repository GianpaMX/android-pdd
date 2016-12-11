package mx.segundamano.gianpa.pdd.timer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerPresenter implements TimerUseCase.Callback {
    private TimerUseCase useCase;
    private TimerView view;

    public TimerPresenter(TimerUseCase useCase) {
        this(useCase, null);
    }

    public TimerPresenter(TimerUseCase useCase, TimerView view) {
        this.useCase = useCase;
        this.view = view;
    }

    public void onStartButtonClick() {
        useCase.start(this);
        view.showStopButton();
    }

    @Override
    public void onTimeUp() {
        view.ringAlarm();
        view.showStartButton();
    }

    @Override
    public void onTick(long remainingTimeInMillis) {
        Date date = new Date(remainingTimeInMillis);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        String remainingTime = formatter.format(date);

        view.onTick(remainingTime);
    }

    @Override
    public void unableToResume() {
        view.showStartButton();
    }

    public TimerView getView() {
        return view;
    }

    public void setView(TimerView view) {
        this.view = view;
    }

    public void resume() {
        view.showStopButton();
        useCase.resume(this);
    }

    public void onStopButtonClick() {
        useCase.stop();
    }
}
