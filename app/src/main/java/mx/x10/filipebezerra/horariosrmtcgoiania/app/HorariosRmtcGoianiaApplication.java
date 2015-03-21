package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AnalyticsTree;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Application class that loads some global configurations.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDefaultFont();

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

    /**
     * Setups the default font of this application. Is executed on (@Link #onCreate).
     * The default font is Roboto-Regular.
     */
    private void initDefaultFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
