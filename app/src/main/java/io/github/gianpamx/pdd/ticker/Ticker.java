package io.github.gianpamx.pdd.ticker;

public interface Ticker {
    void resume(TickListener tickListener);

    void stop();

    interface TickListener {
        void onTick();
    }
}
