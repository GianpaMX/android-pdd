package mx.segundamano.gianpa.pdd.data;

public class Pomodoro {
    public static final int ACTIVE = 1;

    public Integer id;
    public long startTimeInMillis;
    public long endTimeInMillis;
    public int status;

    public long getRemainingTime(long currentTimeMillis) {
        return endTimeInMillis - currentTimeMillis;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public static class Builder {
        private Pomodoro pomodoro;

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

        public Pomodoro build() {
            return pomodoro;
        }
    }
}
