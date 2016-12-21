package mx.segundamano.gianpa.pdd.timer;

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
import mx.segundamano.gianpa.pdd.settings.SettingsActivity;
import mx.segundamano.gianpa.pdd.timer.di.TimerActivityModule;
import mx.segundamano.gianpa.pdd.timer.di.TimerComponent;

public class TimerActivity extends AppCompatActivity implements TimerFragment.TimerFragmentContainer {

    public static final String IS_STOP_INTENT = "IS_STOP_INTENT";
    private static final String STOP_REASON = "STOP_REASON";

    @Inject
    public TimerPresenter presenter;

    private TimerFragment timerFragment;
    private Integer stopReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        inject(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(STOP_REASON) && savedInstanceState.get(STOP_REASON) != null) {
            this.stopReason = savedInstanceState.getInt(STOP_REASON);
        }

        timerFragment = getTimerFragment();
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

    private void inject(TimerActivity timerActivity) {
        getTimerComponent(timerActivity).inject(timerActivity);
    }

    private TimerComponent getTimerComponent(TimerActivity timerActivity) {
        return getAndroidApp().getAndroidAppComponent().timerComponent(new TimerActivityModule(timerActivity));
    }

    private AndroidApp getAndroidApp() {
        return (AndroidApp) getApplication();
    }

    private TimerFragment getTimerFragment() {
        TimerFragment timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container);

        if (timerFragment == null) {
            timerFragment = TimerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.timer_fragment_container, timerFragment).commit();
        }

        return timerFragment;
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
        presenter.setView(timerFragment);

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
}
