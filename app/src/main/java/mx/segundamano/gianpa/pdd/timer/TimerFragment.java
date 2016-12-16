package mx.segundamano.gianpa.pdd.timer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import mx.segundamano.gianpa.pdd.R;

public class TimerFragment extends Fragment implements TimerView {
    public static final String STOP_REASONS = "STOP_REASONS";

    private ViewSwitcher buttonSwitcher;
    private Button startButton;
    private Button stopButton;
    private TextView timerTextView;
    private TimerFragmentContainer container;
    private String[] stopReasons;

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof TimerFragmentContainer) {
            container = (TimerFragmentContainer) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            stopReasons = savedInstanceState.getStringArray(STOP_REASONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_fragment, container, false);

        timerTextView = (TextView) view.findViewById(R.id.timer_text_view);
        startButton = (Button) view.findViewById(R.id.start_button);
        stopButton = (Button) view.findViewById(R.id.stop_button);
        buttonSwitcher = (ViewSwitcher) view.findViewById(R.id.button_switcher);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (container != null) container.onTimerFragmentViewCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(STOP_REASONS, stopReasons);
    }

    @Override
    public void showStopButton() {
        if (buttonSwitcher.getNextView() == stopButton) {
            buttonSwitcher.showNext();
        }
    }

    @Override
    public void showStartButton() {
        if (buttonSwitcher.getNextView() == startButton) {
            buttonSwitcher.showNext();
        }

    }

    @Override
    public void onTick(String remainingTime) {
        timerTextView.setText(remainingTime);
    }

    @Override
    public void ringAlarm() {
        Snackbar.make(getView(), "Time Up", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onStopReasonsReady(String[] stopReasons) {
        this.stopReasons = stopReasons;
    }

    @Override
    public void askStopReasons() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Why did you stop?")
                .setItems(stopReasons, onStopClickListener)
                .setNegativeButton("Undo stop", onUnpauseClickListener)
                .create();

        alertDialog.show();
    }

    private DialogInterface.OnClickListener onStopClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int stopReason) {
            if (container != null) container.onStopClick(stopReason);
        }
    };
    private DialogInterface.OnClickListener onUnpauseClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (container != null) container.onUnpauseClick();
        }
    };

    public interface TimerFragmentContainer {

        void onTimerFragmentViewCreated(Bundle savedInstanceState);

        void onStopClick(int stopReason);

        void onUnpauseClick();
    }
}
