package mx.x10.filipebezerra.horariosrmtcgoiania.application;

import android.support.multidex.MultiDexApplication;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import timber.log.Timber;

/**
 * Application class.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 31/12/2015
 * @since 0.3.0
 */
public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        enableLogging();
    }

    private void enableLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
