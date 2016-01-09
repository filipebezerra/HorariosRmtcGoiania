package mx.x10.filipebezerra.horariosrmtcgoiania.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AnalyticsTree;
import timber.log.Timber;

/**
 * Application class that loads some global configurations.
 *
 * @author Filipe Bezerra
 * @version 2.3, 09/01/2016
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // logging setup
        if (BuildConfig.DEBUG) {
            // detailed logcat logging
            Timber.plant(new Timber.DebugTree());
        } else {
            // crash and error reporting
            Timber.plant(new AnalyticsTree(this));
            if (!Fabric.isInitialized()) {
                Fabric.with(this, new Crashlytics());
            }
        }
    }
}
