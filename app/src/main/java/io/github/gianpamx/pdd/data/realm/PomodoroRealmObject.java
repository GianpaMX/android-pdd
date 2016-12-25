package io.github.gianpamx.pdd.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.github.gianpamx.pdd.data.Pomodoro;

public class PomodoroRealmObject extends RealmObject {
    @PrimaryKey
    public Integer id;
    public long startTimeInMillis;
    public long endTimeInMillis;
    public int status;
    public Integer stopReason;

    public PomodoroRealmObject() {
    }

    public PomodoroRealmObject(Pomodoro pomodoro) {
        this.id = pomodoro.id;
        this.startTimeInMillis = pomodoro.startTimeInMillis;
        this.endTimeInMillis = pomodoro.endTimeInMillis;
        this.status = pomodoro.status;
        this.stopReason = pomodoro.stopReason;
    }

    public Pomodoro toEntity() {
        return Pomodoro.Builder()
                .setId(id)
                .setStartTimeInMillis(startTimeInMillis)
                .setEndTimeInMillis(endTimeInMillis)
                .setStatus(status)
                .setStopReason(stopReason)
                .build();
    }
}
