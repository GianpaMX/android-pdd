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

import mx.segundamano.gianpa.pdd.timer.TimeConstants;
import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;

public class AlarmGatewayImpl implements AlarmGateway {
    public static final int ONE_SECOND = (int) TimeConstants.SECONDS;

    private Context context;
    private Listeners listeners;
    private Handler handler;
    private long startTimeInMillis;
    private long endTimeInMillis;
    private File file;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public AlarmGatewayImpl(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;

        file = getFile();
        listeners = new Listeners();
    }

    @Override
    public void start(long startTimeInMillis, long duration) {
        this.startTimeInMillis = startTimeInMillis;

        startTicker();
        setTimeoutAlarm(duration);

        save(startTimeInMillis, endTimeInMillis);
    }

    private void setTimeoutAlarm(long duration) {
        endTimeInMillis = getTimeUpInMillis(duration);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTimeInMillis, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        if (pendingIntent == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        return pendingIntent;
    }

    @Override
    public long getTimeUpInMillis(long duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimeInMillis);
        calendar.add(Calendar.MILLISECOND, (int) duration);
        return calendar.getTimeInMillis();
    }

    @Override
    public long getRemainingTime() {
        return endTimeInMillis - startTimeInMillis;
    }

    private void startTicker() {
        handler = new Handler();
        handler.postDelayed(ticker, ONE_SECOND);
    }

    @Override
    public void resume() {
        if (!isActive()) {
            listeners.onError(new UnableToResume());
            return;
        }

        if (startTimeInMillis == 0) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[8];
                inputStream.read(bytes);
                startTimeInMillis = ByteUtils.bytesToLong(bytes);
                endTimeInMillis = ByteUtils.bytesToLong(bytes);
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

    public boolean isActive() {
        return handler != null || file.exists();
    }

    @Override
    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(ticker);
            handler = null;
        }

        alarmManager.cancel(getPendingIntent());

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
    public void removeTickerListener(TickListener tickListener) {
        listeners.remove(tickListener);
    }

    @Override
    public void addTimeUpListener(@NonNull TimeUpListener listener) {
        listeners.add(listener);
    }

    @Override
    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    @Override
    public long getEndTimeInMillis() {
        return endTimeInMillis;
    }

    private void save(long startTimeInMillis, long duration) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(ByteUtils.longToBytes(startTimeInMillis));
            outputStream.write(ByteUtils.longToBytes(duration));

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
