package mx.segundamano.gianpa.pdd.timer.alarmgateway;

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
import java.util.Calendar;

import mx.segundamano.gianpa.pdd.timer.AlarmGateway;

public class AlarmGatewayImpl implements AlarmGateway {
    public static final int ONE_SECOND = 1000;

    private Context context;
    private TickListener listener;
    private Handler handler;
    private long startTimeInMillis;
    private File file;
    private AlarmManager alarmManager;

    public AlarmGatewayImpl(Context context) {
        this.context = context;
        file = getFile();
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
            if (listener != null) listener.onError(new UnableToResume());
            return;
        }

        if (startTimeInMillis == 0) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[8];
                inputStream.read(bytes);
                startTimeInMillis = ByteUtils.bytesToLong(bytes);
            } catch (FileNotFoundException e) {
                if (listener != null) listener.onError(new UnableToResume());
            } catch (IOException e) {
                if (listener != null) listener.onError(new UnableToResume());
            }
        }

        if (handler == null) {
            startTicker();
        }
    }

    @Override
    public void stop() {
        if (handler != null) handler.removeCallbacks(ticker);

        if (file.exists()) file.delete();
    }

    @Override
    public void timeUp() {
        stop();

        if (listener != null) {
            listener.onTimeUp();
        }
    }

    @NonNull
    private File getFile() {
        return new File(context.getFilesDir(), "pdd-start.time");
    }

    private Runnable ticker = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, ONE_SECOND);
            if (listener != null) listener.onTick();
        }
    };

    @Override
    public void setTickListener(TickListener listener) {
        this.listener = listener;
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
            if (listener != null) listener.onError(new UnableToSaveStartTime());
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
}
