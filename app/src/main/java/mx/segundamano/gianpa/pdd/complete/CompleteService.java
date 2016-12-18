package mx.segundamano.gianpa.pdd.complete;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.complete.di.CompleteComponent;
import mx.segundamano.gianpa.pdd.complete.di.CompleteServiceModule;

public class CompleteService extends IntentService {

    public static final String TAG = CompleteService.class.getSimpleName();
    public static final String IS_COMPLETE = "IS_COMPLETE";

    public static Intent newIntent(Context context, boolean isComplete) {
        Intent intent = new Intent(context, CompleteService.class);
        intent.putExtra(IS_COMPLETE, isComplete);
        return intent;
    }

    @Inject
    public CompleteUseCase completeUseCase;

    public CompleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        inject(this);
        completeUseCase.complete(intent.getBooleanExtra(IS_COMPLETE, true));
    }

    private void inject(CompleteService completeService) {
        getCompleteComponent(completeService).inject(completeService);
    }

    private CompleteComponent getCompleteComponent(CompleteService completeService) {
        return getAndroidApp().getAndroidAppComponent().completeComponent(new CompleteServiceModule(completeService));
    }

    private AndroidApp getAndroidApp() {
        return (AndroidApp) getApplication();
    }
}
