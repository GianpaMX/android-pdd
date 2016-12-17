package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.ticker.Ticker;

public class WakeupUseCase {
    private Ticker ticker;
    private Callback callback;

    public WakeupUseCase(Ticker ticker) {
        this.ticker = ticker;
    }

    public void timeUp(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onTimeUp();
    }
}
