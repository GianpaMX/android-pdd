package mx.segundamano.gianpa.pdd.timer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.segundamano.gianpa.pdd.R;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.timer_fragment_container, new TimerFragment()).commit();
        }
    }
}
