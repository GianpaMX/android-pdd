package mx.segundamano.gianpa.pdd.timer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.timer.di.TimerActivityModule;
import mx.segundamano.gianpa.pdd.timer.di.TimerComponent;

public class TimerActivity extends AppCompatActivity implements TimerFragment.TimerFragmentContainer {

    public static final String IS_STOP_INTENT = "IS_STOP_INTENT";

    @Inject
    public TimerPresenter presenter;

    private TimerFragment timerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        inject(this);

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
        TimerFragment timerFragment = timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container);

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
        presenter.onStopButtonClick();
    }

    @Override
    public void onTimerFragmentViewCreated(Bundle savedInstanceState) {
        presenter.setView(timerFragment);

        if (getIntent().getExtras() != null && getIntent().getBooleanExtra(IS_STOP_INTENT, false)) {
            presenter.onStopButtonClick();
        }
    }

    @Override
    public void onStopReasonClick(int stopReason) {
        presenter.onStopReasonClick(stopReason);
        if (getIntent().getExtras() != null && getIntent().getBooleanExtra(IS_STOP_INTENT, false)) {
            getIntent().getExtras().remove(IS_STOP_INTENT);
        }
    }

    @Override
    public void onUnpauseClick() {
        if (getIntent().getExtras() != null && getIntent().getBooleanExtra(IS_STOP_INTENT, false)) {
            getIntent().getExtras().remove(IS_STOP_INTENT);
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
