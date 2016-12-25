package io.github.gianpamx.pdd.data;

public class Pomodoro {
    public static final int ACTIVE = 1;
    public static final int INTERRUPTED = 2;
    public static final int ERROR = 3;
    public static final int COMPLETE = 4;
    public static final int DISCARDED = 5;
    public static final int TIME_UP = 6;

    public Integer id;
    public long startTimeInMillis;
    public long endTimeInMillis;
    public int status;
    public Integer stopReason;

    public long getRemainingTime(long currentTimeMillis) {
        return endTimeInMillis - currentTimeMillis;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public static class Builder {
        private Pomodoro pomodoro;

        public Builder() {
            pomodoro = new Pomodoro();
        }

        public Builder setId(Integer id) {
            pomodoro.id = id;
            return this;
        }

        public Builder setStartTimeInMillis(long startTimeInMillis) {
            pomodoro.startTimeInMillis = startTimeInMillis;
            return this;
        }

        public Builder setEndTimeInMillis(long endTimeInMillis) {
            pomodoro.endTimeInMillis = endTimeInMillis;
            return this;
        }

        public Builder setStatus(int status) {
            pomodoro.status = status;
            return this;
        }

        public Builder setStopReason(Integer stopReason) {
            pomodoro.stopReason = stopReason;
            return this;
        }

        public Pomodoro build() {
            return pomodoro;
        }
    }
}
