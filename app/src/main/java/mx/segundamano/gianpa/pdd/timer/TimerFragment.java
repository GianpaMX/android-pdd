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
    private ViewSwitcher buttonSwitcher;
    private Button startButton;
    private Button stopButton;
    private TextView timerTextView;
    private TimerFragmentContainer container;
    private AlertDialog alertDialog;

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
    public void askStopReasons(String[] stopReasons) {
        if (alertDialog != null) alertDialog.dismiss();
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.stop_reasons_dialog_title)
                .setItems(stopReasons, onStopClickListener)
                .setNegativeButton(R.string.stop_reasons_dialog_cancel_text, onUnpauseClickListener)
                .create();

        alertDialog.show();
    }

    @Override
    public void showErrorDialog() {
        if (alertDialog != null) alertDialog.dismiss();
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pomodoro_resume_dialog_error_title)
                .setItems(R.array.pomodoro_error_actions, onErrorActionSelectedListener)
                .setCancelable(false)
                .create();

        alertDialog.show();
    }

    @Override
    public void askComplete() {
        if (alertDialog != null) alertDialog.dismiss();
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pomodoro_complete_dialog_title)
                .setItems(R.array.pomodoro_complete_actions, onCompleteClickListener)
                .setCancelable(false)
                .create();

        alertDialog.show();
    }

    private DialogInterface.OnClickListener onStopClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int stopReason) {
            alertDialog = null;
            if (container != null) container.onStopReasonClick(stopReason);
        }
    };
    private DialogInterface.OnClickListener onUnpauseClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            alertDialog = null;
            if (container != null) container.onUnpauseClick();
        }
    };

    private DialogInterface.OnClickListener onErrorActionSelectedListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            alertDialog = null;
            if (container != null) container.onErrorActionSelected(i);
        }
    };

    private DialogInterface.OnClickListener onCompleteClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            alertDialog = null;
            if (container != null) container.onCompleteActionSelected(i);
        }
    };

    public interface TimerFragmentContainer {

        void onTimerFragmentViewCreated(Bundle savedInstanceState);

        void onStopReasonClick(int stopReason);

        void onUnpauseClick();

        void onErrorActionSelected(int errorAction);

        void onCompleteActionSelected(int completeAction);
    }
}
