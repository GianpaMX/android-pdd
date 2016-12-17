package mx.segundamano.gianpa.pdd.ticker;

import android.os.Handler;

public class TickerImpl implements Ticker {

    private static final long ONE_SECOND = 1000;

    private Handler handler;
    private TickListener tickListener;
    private boolean isTickerActive;

    public TickerImpl() {
        isTickerActive = false;
    }

    @Override
    public void resume(TickListener tickListener) {
        this.tickListener = tickListener;

        isTickerActive = true;
        handler = new Handler();
        handler.postDelayed(ticker, 0);
    }

    @Override
    public void stop() {
        isTickerActive = false;
    }

    private Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (!isTickerActive) {
                handler = null;
                return;
            }

            handler.postDelayed(this, ONE_SECOND);
            tickListener.onTick();
        }
    };
}
