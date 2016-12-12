package mx.segundamano.gianpa.pdd.alarmgateway;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;

public class AlarmGatewayImpl implements AlarmGateway {
    public static final int ONE_SECOND = 1000;

    private Context context;
    private Listeners listeners;
    private Handler handler;
    private long startTimeInMillis;
    private File file;
    private AlarmManager alarmManager;

    public AlarmGatewayImpl(Context context) {
        this.context = context;
        file = getFile();
        listeners = new Listeners();
    }

    @Override
    public void start(long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;

        saveStartTime(startTimeInMillis);

        startTicker();

        setTimeoutAlarm();
    }

    private void setTimeoutAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 15);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void startTicker() {
        handler = new Handler();
        handler.postDelayed(ticker, ONE_SECOND);
    }

    @Override
    public void resume() {
        if (!file.exists()) {
            listeners.onError(new UnableToResume());
            return;
        }

        if (startTimeInMillis == 0) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[8];
                inputStream.read(bytes);
                startTimeInMillis = ByteUtils.bytesToLong(bytes);
            } catch (FileNotFoundException e) {
                listeners.onError(new UnableToResume());
            } catch (IOException e) {
                listeners.onError(new UnableToResume());
            }
        }

        if (handler == null) {
            startTicker();
        }
    }

    @Override
    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(ticker);
            handler = null;
        }

        if (file.exists()) file.delete();
    }

    @Override
    public void timeUp() {
        stop();

        listeners.onTimeUp();
    }

    @NonNull
    private File getFile() {
        return new File(context.getFilesDir(), "pdd-start.time");
    }

    private Runnable ticker = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, ONE_SECOND);
            listeners.onTick();
        }
    };

    @Override
    public void addTickListener(@NonNull TickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void addTimeUpListener(@NonNull TimeUpListener listener) {
        listeners.add(listener);
    }

    @Override
    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    private void saveStartTime(long startTimeInMillis) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(ByteUtils.longToBytes(startTimeInMillis));

            outputStream.close();
        } catch (IOException e) {
            listeners.onError(new UnableToSaveStartTime());
        }
    }

    public static class ByteUtils {
        public static byte[] longToBytes(long l) {
            byte[] result = new byte[8];
            for (int i = 7; i >= 0; i--) {
                result[i] = (byte) (l & 0xFF);
                l >>= 8;
            }
            return result;
        }

        public static long bytesToLong(byte[] b) {
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result <<= 8;
                result |= (b[i] & 0xFF);
            }
            return result;
        }
    }

    private static class Listeners extends ArrayList<TimeUpListener> implements TickListener {

        @Override
        public void onTick() {
            for (TimeUpListener listener : this) {
                if (listener instanceof TickListener) ((TickListener) listener).onTick();
            }
        }

        @Override
        public void onTimeUp() {
            for (TimeUpListener listener : this) {
                listener.onTimeUp();
            }
        }

        @Override
        public void onError(Throwable error) {
            for (TimeUpListener listener : this) {
                if (listener instanceof TickListener) ((TickListener) listener).onError(error);
            }
        }
    }
}
