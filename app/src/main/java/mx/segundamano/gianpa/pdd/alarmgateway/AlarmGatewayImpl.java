package mx.segundamano.gianpa.pdd.alarmgateway;

import android.os.Handler;

public class AlarmGatewayImpl implements AlarmGateway {

    private static final long ONE_SECOND = 1000;

    private Handler handler;
    private TickListener tickListener;
    private boolean isTickerActive;

    public AlarmGatewayImpl() {
        isTickerActive = false;
    }

    @Override
    public void resumeTicker(TickListener tickListener) {
        this.tickListener = tickListener;

        isTickerActive = true;
        handler = new Handler();
        handler.postDelayed(ticker, 0);
    }

    @Override
    public void stopTicker() {
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
