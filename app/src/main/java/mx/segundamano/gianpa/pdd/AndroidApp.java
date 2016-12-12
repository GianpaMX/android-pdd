package mx.segundamano.gianpa.pdd;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.di.AndroidAppComponent;
import mx.segundamano.gianpa.pdd.di.AndroidAppModule;
import mx.segundamano.gianpa.pdd.di.DaggerAndroidAppComponent;
import mx.segundamano.gianpa.pdd.notify.NotifyUseCase;

public class AndroidApp extends Application {
    public static final String TAG = AndroidApp.class.getSimpleName();

    @Inject
    public NotifyUseCase notifyUseCase;

    private AndroidAppComponent androidAppComponent;
    private AppCompatActivity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        getAndroidAppComponent().inject(this);
    }

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

        if(currentActivity == null) {
            notifyUseCase.background();
        } else {
            notifyUseCase.foreground();
        }
    }

    public AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }
}
