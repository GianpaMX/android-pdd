package io.github.gianpamx.pdd.data;

public interface BreakTimerRepository {
    Break findBreak();

    void persist(Break aBreak);
}
