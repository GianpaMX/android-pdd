package mx.segundamano.gianpa.pdd.data;

import java.util.Calendar;

public class PomodoroRepository {
    public void findActivePomodoro(Callback<Pomodoro> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 11, 16, 10, 0, 0);

        Pomodoro pomodoro = new Pomodoro();
        pomodoro.endTimeMillis = calendar.getTimeInMillis();
        callback.onSuccess(pomodoro);
    }

    public interface Callback<T> {
        void onSuccess(T result);
    }
}
