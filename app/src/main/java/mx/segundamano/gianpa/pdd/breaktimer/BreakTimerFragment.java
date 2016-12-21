package mx.segundamano.gianpa.pdd.breaktimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.segundamano.gianpa.pdd.R;

public class BreakTimerFragment extends Fragment {
    public static BreakTimerFragment newInstance() {
        return new BreakTimerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.break_timer_fragment, container, false);
        return view;
    }
}
