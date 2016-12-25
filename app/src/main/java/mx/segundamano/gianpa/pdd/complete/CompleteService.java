package mx.segundamano.gianpa.pdd.complete;

import android.app.IntentService;
import android.content.Intent;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.complete.di.CompleteComponent;
import mx.segundamano.gianpa.pdd.complete.di.CompleteServiceModule;

public class CompleteService extends IntentService {

    public static final String TAG = CompleteService.class.getSimpleName();

    @Inject
    public CompleteUseCase completeUseCase;

    public CompleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        inject(this);

        if (getString(R.string.COMPLETE_AND_COUND).equals(intent.getAction())) {
            completeUseCase.complete(true);
        } else if (getString(R.string.COMPLETE_AND_DISCARD).equals(intent.getAction())) {
            completeUseCase.complete(false);
        }
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
