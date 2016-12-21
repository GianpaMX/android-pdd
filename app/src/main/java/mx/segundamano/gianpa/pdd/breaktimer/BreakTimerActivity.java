package mx.segundamano.gianpa.pdd.breaktimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.breaktimer.di.BreakTimerActivityModule;
import mx.segundamano.gianpa.pdd.breaktimer.di.BreakTimerComponent;

public class BreakTimerActivity extends AppCompatActivity {

    public static final String IS_STOP_INTENT = "IS_STOP_INTENT";

    private BreakTimerFragment breakTimerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.break_timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("Break");

        breakTimerFragment = getBreakTimerFragment();

        inject(this);
    }

    private BreakTimerFragment getBreakTimerFragment() {
        BreakTimerFragment breakTimerFragment = (BreakTimerFragment) getSupportFragmentManager().findFragmentById(R.id.break_timer_fragment_container);

        if (breakTimerFragment == null) {
            breakTimerFragment = BreakTimerFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.break_timer_fragment_container, breakTimerFragment).commit();
        }

        return breakTimerFragment;
    }

    private void inject(BreakTimerActivity breakTimerActivity) {
        getBreakTimerComponent(breakTimerActivity).inject(breakTimerActivity);
    }

    private BreakTimerComponent getBreakTimerComponent(BreakTimerActivity breakTimerActivity) {
        return getAndroidApp().getAndroidAppComponent().breakTimerComponent(new BreakTimerActivityModule(breakTimerActivity));
    }

    protected AndroidApp getAndroidApp() {
        return (AndroidApp) getApplication();
    }
}
