package mx.segundamano.gianpa.pdd.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mx.segundamano.gianpa.pdd.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSettingsFragment();
    }

    private SettingsFragment getSettingsFragment() {
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.settings_fragment_container);

        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.settings_fragment_container, settingsFragment).commit();
        }

        return settingsFragment;
    }

}
