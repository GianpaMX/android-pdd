package mx.segundamano.gianpa.pdd.data;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import mx.segundamano.gianpa.pdd.data.realm.PomodoroRealmObject;

public class PomodoroRepository {
    private Realm realm;

    public PomodoroRepository(Realm realm) {
        this.realm = realm;
    }

    public void findActivePomodoro(Callback<Pomodoro> callback) {
        RealmResults<PomodoroRealmObject> results = realm
                .where(PomodoroRealmObject.class)
                .equalTo("status", Pomodoro.ACTIVE)
                .findAllSorted("startTimeInMillis", Sort.DESCENDING);

        callback.onSuccess(results.size() > 0 ? results.first().toEntity() : null);
    }

    public void persist(final Pomodoro pomodoro, final Callback<Pomodoro> callback) {
        final PomodoroRealmObject pomodoroRealmObject = new PomodoroRealmObject(pomodoro);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (pomodoroRealmObject.id == null) pomodoroRealmObject.id = getNextKey(realm);
                realm.insertOrUpdate(pomodoroRealmObject);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onSuccess(pomodoroRealmObject.toEntity());
            }
        });
    }

    private Integer getNextKey(Realm realm) {
        Number number = realm.where(PomodoroRealmObject.class).max("id");
        return 1 + (number == null ? 0 : number.intValue());
    }

    public void findTimeUpPomodoro(Callback<Pomodoro> callback) {
        RealmResults<PomodoroRealmObject> results = realm
                .where(PomodoroRealmObject.class)
                .equalTo("status", Pomodoro.TIME_UP)
                .findAllSorted("startTimeInMillis", Sort.DESCENDING);

        callback.onSuccess(results.size() > 0 ? results.first().toEntity() : null);
    }

    public interface Callback<T> {
        void onSuccess(T result);
    }
}
