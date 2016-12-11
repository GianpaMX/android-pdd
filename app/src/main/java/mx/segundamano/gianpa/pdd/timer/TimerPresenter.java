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

    public TimerView getView() {
        return view;
    }

    public void setView(TimerView view) {
        this.view = view;
    }

    public void resume() {
        if (view != null) view.showStopButton();
        useCase.resume(this);
    }

    public void onStopButtonClick() {
        useCase.stop();
    }
}
