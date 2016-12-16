package mx.segundamano.gianpa.pdd.alarmgateway;

public interface AlarmGateway {
    void resumeTicker(TickListener tickListener);

    void stopTicker();

    interface TickListener {
        void onTick();
    }
}
