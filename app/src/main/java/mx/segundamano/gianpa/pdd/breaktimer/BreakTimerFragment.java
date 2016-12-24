package mx.segundamano.gianpa.pdd.breaktimer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mx.segundamano.gianpa.pdd.R;

public class BreakTimerFragment extends Fragment implements BreakTimerView {

    private TimerFragmentContainer container;

    private TextView timerTextView;

    public static BreakTimerFragment newInstance() {
        return new BreakTimerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof TimerFragmentContainer) {
            container = (TimerFragmentContainer) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.break_timer_fragment, container, false);

        timerTextView = (TextView) view.findViewById(R.id.timer_text_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (container != null) container.onTimerFragmentViewCreated(savedInstanceState);
    }

    @Override
    public void onTick(String remainingTime) {
        timerTextView.setText(remainingTime);
    }

    @Override
    public void onCompleted(boolean startPomodoro) {
        if (container != null) container.onCompleted(startPomodoro);
    }

    public interface TimerFragmentContainer {
        void onTimerFragmentViewCreated(Bundle savedInstanceState);

        void onStartButtonClick(View view);

        void onCompleted(boolean startPomodoro);
    }
}
