package mx.segundamano.gianpa.pdd;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import mx.segundamano.gianpa.pdd.di.AndroidAppComponent;
import mx.segundamano.gianpa.pdd.di.AndroidAppModule;
import mx.segundamano.gianpa.pdd.di.DaggerAndroidAppComponent;

public class AndroidApp extends Application {
    public static final String TAG = AndroidApp.class.getSimpleName();

    private AndroidAppComponent androidAppComponent;
    private AppCompatActivity currentActivity;

    public AndroidAppComponent getAndroidAppComponent() {
        if (androidAppComponent == null) {
            androidAppComponent = DaggerAndroidAppComponent.builder()
                    .androidAppModule(new AndroidAppModule(this))
                    .build();
        }
        return androidAppComponent;
    }

    public void setAppComponent(AndroidAppComponent heyComponent) {
        this.androidAppComponent = heyComponent;
    }

    public void setCurrentActivity(AppCompatActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }
}
