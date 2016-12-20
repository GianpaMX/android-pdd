package mx.segundamano.gianpa.pdd.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.data.SettingsRepository;

public class SettingsRepositoryimpl implements SettingsRepository {

    public static final String SETTINGS_POMODORO_TIME = "SETTINGS_POMODORO_TIME";

    private final Context context;

    private SharedPreferences sharedPref;

    public SettingsRepositoryimpl(Context context) {
        this.context = context;
    }

    @Override
    public long findPomodoroTime() {
        String pomodoroTime = getSharedPreferences().getString(SETTINGS_POMODORO_TIME, context.getString(R.string.settings_pomodoro_time_default_value));
        return Long.parseLong(pomodoroTime);
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return sharedPref;
    }
}
