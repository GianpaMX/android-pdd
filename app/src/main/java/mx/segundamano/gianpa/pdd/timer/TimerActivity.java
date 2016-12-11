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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        inject(this);

        TimerFragment timerFragment = getTimerFragment(savedInstanceState);
        presenter.setView(timerFragment);
    }

    private void inject(TimerActivity timerActivity) {
        getTimerComponent(timerActivity).inject(timerActivity);
    }

    private TimerComponent getTimerComponent(TimerActivity timerActivity) {
        return ((AndroidApp) timerActivity.getApplication()).getAndroidAppComponent().timerComponent(new TimerActivityModule(timerActivity));
    }

    private TimerFragment getTimerFragment(Bundle savedInstanceState) {
        TimerFragment timerFragment;
        if (savedInstanceState == null) {
            timerFragment = new TimerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.timer_fragment_container, timerFragment).commit();
        } else {
            timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container);
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
        if (savedInstanceState != null) {
            presenter.resume();
        }
    }
}
