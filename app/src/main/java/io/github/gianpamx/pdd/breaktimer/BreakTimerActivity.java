package io.github.gianpamx.pdd.breaktimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import io.github.gianpamx.pdd.AndroidApp;
import io.github.gianpamx.pdd.R;
import io.github.gianpamx.pdd.breaktimer.di.BreakTimerActivityModule;
import io.github.gianpamx.pdd.breaktimer.di.BreakTimerComponent;

public class BreakTimerActivity extends AppCompatActivity implements BreakTimerFragment.TimerFragmentContainer {

    public static final String IS_START_INTENT = "IS_START_INTENT";

    @Inject
    public BreakTimerPresenter presenter;

    private BreakTimerFragment breakTimerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.break_timer_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Break");

        inject(this);

        breakTimerFragment = getBreakTimerFragment();

        inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onActivityResume();

        if (getIntent().getExtras() != null && getIntent().getBooleanExtra(IS_START_INTENT, false)) {
            presenter.onStartButtonClick();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onActivityPause();
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

    @Override
    public void onTimerFragmentViewCreated(Bundle savedInstanceState) {
        presenter.setView(breakTimerFragment);
    }

    @Override
    public void onStartButtonClick(View view) {
        presenter.onStartButtonClick();
    }

    @Override
    public void onCompleted(boolean startPomodoro) {
        finish();
    }

    @Override
    public void onBackPressed() {
        presenter.onCancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onCancel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
