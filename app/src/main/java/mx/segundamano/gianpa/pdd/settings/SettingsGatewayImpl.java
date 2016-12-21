package mx.segundamano.gianpa.pdd.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.data.Break;
import mx.segundamano.gianpa.pdd.data.SettingsGateway;

public class SettingsGatewayImpl implements SettingsGateway {

    public static final String SETTINGS_POMODORO_TIME = "SETTINGS_POMODORO_TIME";
    private static final String SETTINGS_BREAK_TIME = "SETTINGS_BREAK_TIME";

    public static final String SETTINGS_BREAK_STATUS = "SETTINGS_BREAK_STATUS";
    private static final String SETTINGS_BREAK_START_TIME_IN_MILLIS = "SETTINGS_BREAK_START_TIME_IN_MILLIS";
    private static final String SETTINGS_BREAK_END_TIME_IN_MILLIS = "SETTINGS_BREAK_END_TIME_IN_MILLIS";

    private final Context context;

    private SharedPreferences sharedPref;

    public SettingsGatewayImpl(Context context) {
        this.context = context;
    }

    @Override
    public long readPomodoroTime() {
        String pomodoroTime = getSharedPreferences().getString(SETTINGS_POMODORO_TIME, context.getString(R.string.settings_pomodoro_time_default_value));
        return Long.parseLong(pomodoroTime);
    }

    @Override
    public long readBreakTime() {
        String breakTime = getSharedPreferences().getString(SETTINGS_BREAK_TIME, context.getString(R.string.settings_break_time_default_value));
        return Long.parseLong(breakTime);
    }

    @Override
    public Break readBreak() {
        Break breakEntity = new Break();
        breakEntity.status = getSharedPreferences().getInt(SETTINGS_BREAK_STATUS, Break.INACTIVE);
        breakEntity.startTimeInMillis = getSharedPreferences().getLong(SETTINGS_BREAK_START_TIME_IN_MILLIS, 0);
        breakEntity.endTimeInMillis = getSharedPreferences().getLong(SETTINGS_BREAK_END_TIME_IN_MILLIS, 0);
        return breakEntity;
    }

    @Override
    public void writeBreak(Break aBreak) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.putInt(SETTINGS_BREAK_STATUS, Break.INACTIVE);

        editor.commit();
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return sharedPref;
    }
}
