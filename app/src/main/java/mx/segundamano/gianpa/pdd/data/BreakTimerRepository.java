package mx.segundamano.gianpa.pdd.data;

public interface BreakTimerRepository {
    Break findBreak();

    void persist(Break aBreak);
}
