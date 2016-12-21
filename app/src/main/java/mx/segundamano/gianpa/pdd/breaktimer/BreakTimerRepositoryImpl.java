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
        Break aBreak = settingsGateway.readBreak();

        if(aBreak.status == Break.INACTIVE) return null;

        return aBreak;
    }

    @Override
    public void persist(Break aBreak) {
        settingsGateway.writeBreak(aBreak);
    }
}
