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
    protected void onStart() {
        super.onStart();

        getAndroidApp().setCurrentActivity(this);
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
    protected void onStop() {
        super.onStop();

        getAndroidApp().setCurrentActivity(null);
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
        presenter.onStopButtonClick();
    }

    @Override
    public void onTimerFragmentViewCreated(Bundle savedInstanceState) {
        presenter.setView(timerFragment);
    }

    @Override
    public void onStopClick(int stopReason) {
    }

    @Override
    public void onUnpauseClick() {
    }
}
