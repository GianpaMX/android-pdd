package mx.segundamano.gianpa.pdd.ticker;

public interface Ticker {
    void resume(TickListener tickListener);

    void stop();

    interface TickListener {
        void onTick();
    }
}
