package io.github.gianpamx.pdd.data;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.github.gianpamx.pdd.data.realm.PomodoroRealmObject;

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

    /**
     * Asynchronous
     *
     * @param pomodoro
     * @param callback
     */
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

    /**
     * Synchronous
     *
     * @param pomodoro
     * @return
     */
    public Pomodoro persist(Pomodoro pomodoro) {
        final PomodoroRealmObject pomodoroRealmObject = new PomodoroRealmObject(pomodoro);

        realm.beginTransaction();
        if (pomodoroRealmObject.id == null) pomodoroRealmObject.id = getNextKey(realm);
        realm.insertOrUpdate(pomodoroRealmObject);
        realm.commitTransaction();

        return pomodoroRealmObject.toEntity();
    }

    private Integer getNextKey(Realm realm) {
        Number number = realm.where(PomodoroRealmObject.class).max("id");
        return 1 + (number == null ? 0 : number.intValue());
    }

    /**
     * Asynchronous
     * @param callback
     */
    public void findTimeUpPomodoro(Callback<Pomodoro> callback) {
        RealmResults<PomodoroRealmObject> results = queryTimeUpPomodoros();
        callback.onSuccess(results.size() > 0 ? results.first().toEntity() : null);
    }

    /**
     * Synchronous
     * @return
     */
    public Pomodoro findTimeUpPomodoro() {
        RealmResults<PomodoroRealmObject> results = queryTimeUpPomodoros();
        return results.size() > 0 ? results.first().toEntity() : null;
    }

    @NonNull
    public RealmResults<PomodoroRealmObject> queryTimeUpPomodoros() {
        return realm
                .where(PomodoroRealmObject.class)
                .equalTo("status", Pomodoro.TIME_UP)
                .findAllSorted("startTimeInMillis", Sort.DESCENDING);
    }

    public interface Callback<T> {
        void onSuccess(T result);
    }
}
