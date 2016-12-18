package mx.segundamano.gianpa.pdd;

import android.app.Application;

import io.realm.Realm;
import mx.segundamano.gianpa.pdd.di.AndroidAppComponent;
import mx.segundamano.gianpa.pdd.di.AndroidAppModule;
import mx.segundamano.gianpa.pdd.di.DaggerAndroidAppComponent;

public class AndroidApp extends Application {
    public static final String TAG = AndroidApp.class.getSimpleName();

    private AndroidAppComponent androidAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public AndroidAppComponent getAndroidAppComponent() {
        if (androidAppComponent == null) {
            androidAppComponent = DaggerAndroidAppComponent.builder()
                    .androidAppModule(new AndroidAppModule(this))
                    .build();
        }
        return androidAppComponent;
    }
}
