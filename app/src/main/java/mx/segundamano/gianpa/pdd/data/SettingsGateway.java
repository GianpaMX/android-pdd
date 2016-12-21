package mx.segundamano.gianpa.pdd.data;

public interface SettingsGateway {
    long readPomodoroTime();

    long readBreakTime();

    Break readBreak();

    void writeBreak(Break aBreak);
}
