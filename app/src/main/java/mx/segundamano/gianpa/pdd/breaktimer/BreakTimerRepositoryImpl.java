package mx.segundamano.gianpa.pdd.breaktimer;

import mx.segundamano.gianpa.pdd.data.Break;
import mx.segundamano.gianpa.pdd.data.BreakTimerRepository;
import mx.segundamano.gianpa.pdd.data.SettingsGateway;

public class BreakTimerRepositoryImpl implements BreakTimerRepository {

    private SettingsGateway settingsGateway;

    public BreakTimerRepositoryImpl(SettingsGateway settingsGateway) {
        this.settingsGateway = settingsGateway;
    }

    @Override
    public Break findBreak() {
        return settingsGateway.readBreak();
    }

    @Override
    public void persist(Break aBreak) {
        settingsGateway.writeBreak(aBreak);
    }
}
