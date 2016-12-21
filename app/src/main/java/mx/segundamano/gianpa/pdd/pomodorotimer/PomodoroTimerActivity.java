package mx.segundamano.gianpa.pdd.pomodorotimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerActivity;
import mx.segundamano.gianpa.pdd.pomodorotimer.di.PomodoroTimerActivityModule;
import mx.segundamano.gianpa.pdd.pomodorotimer.di.PomodoroTimerComponent;
import mx.segundamano.gianpa.pdd.settings.SettingsActivity;

public class PomodoroTimerActivity extends AppCompatActivity implements PomodoroTimerFragment.TimerFragmentContainer {

    private static final String STOP_REASON = "STOP_REASON";
    public static final String IS_STOP_INTENT = "IS_STOP_INTENT";

    protected PomodoroTimerFragment pomodoroTimerFragment;

    @Inject
    public PomodoroTimerPresenter presenter;

    private Integer stopReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pomodoro_timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        pomodoroTimerFragment = getPomodoroTimerFragment();

        setTitle("Pomodoro");

        inject(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(STOP_REASON) && savedInstanceState.get(STOP_REASON) != null) {
            this.stopReason = savedInstanceState.getInt(STOP_REASON);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onActivityPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PomodoroTimerFragment getPomodoroTimerFragment() {
        PomodoroTimerFragment pomodoroTimerFragment = (PomodoroTimerFragment) getSupportFragmentManager().findFragmentById(R.id.pomodoro_timer_fragment_container);

        if (pomodoroTimerFragment == null) {
            pomodoroTimerFragment = PomodoroTimerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.pomodoro_timer_fragment_container, pomodoroTimerFragment).commit();
        }

        return pomodoroTimerFragment;
    }

    private void inject(PomodoroTimerActivity pomodoroTimerActivity) {
        getPomodoroTimerComponent(pomodoroTimerActivity).inject(pomodoroTimerActivity);
    }

    private PomodoroTimerComponent getPomodoroTimerComponent(PomodoroTimerActivity pomodoroTimerActivity) {
        return getAndroidApp().getAndroidAppComponent().pomodoroTimerComponent(new PomodoroTimerActivityModule(pomodoroTimerActivity));
    }

    protected AndroidApp getAndroidApp() {
        return (AndroidApp) getApplication();
    }

    public void onStartButtonClick(View view) {
        presenter.onStartButtonClick();
    }

    public void onStopButtonClick(View view) {
        getIntent().putExtra(IS_STOP_INTENT, true);
        this.stopReason = null;
        presenter.onStopButtonClick();
    }

    @Override
    public void onTimerFragmentViewCreated(Bundle savedInstanceState) {
        presenter.setView(pomodoroTimerFragment);

        if (stopReason == null && getIntent().getExtras() != null && getIntent().getBooleanExtra(IS_STOP_INTENT, false)) {
            presenter.onStopButtonClick();
        }
    }

    @Override
    public void onStopReasonClick(int stopReason) {
        presenter.onStopReasonClick(stopReason);
        this.stopReason = stopReason;
    }

    @Override
    public void onUnpauseClick() {
        this.stopReason = -1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (stopReason != null) {
            outState.putInt(STOP_REASON, stopReason);
        } else if (outState.containsKey(STOP_REASON)) {
            outState.remove(STOP_REASON);
        }
    }

    @Override
    public void onErrorActionSelected(int errorAction) {
        presenter.stopOnError(errorAction);
    }

    @Override
    public void onCompleteActionSelected(int completeAction) {
        presenter.onCompleteActionClick(completeAction);
    }

    @Override
    public void onCompleted() {
        Intent breakTimerActivityIntent = new Intent(this, BreakTimerActivity.class);
        startActivity(breakTimerActivityIntent);
    }
}
