package io.github.gianpamx.pdd;

import android.app.Application;

import io.realm.Realm;
import io.github.gianpamx.pdd.di.AndroidAppComponent;
import io.github.gianpamx.pdd.di.AndroidAppModule;
import io.github.gianpamx.pdd.di.DaggerAndroidAppComponent;

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
