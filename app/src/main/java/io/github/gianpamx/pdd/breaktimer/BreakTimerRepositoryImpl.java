package io.github.gianpamx.pdd.breaktimer;

import io.github.gianpamx.pdd.data.Break;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;

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
