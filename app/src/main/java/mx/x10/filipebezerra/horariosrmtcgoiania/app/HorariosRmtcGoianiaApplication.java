package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.StrictMode;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.AnalyticsTree;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.AndroidUtils;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.os.StrictMode.ThreadPolicy;
import static android.os.StrictMode.VmPolicy;
import static android.os.StrictMode.setThreadPolicy;
import static android.os.StrictMode.setVmPolicy;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application {
    private static final String LOG_TAG = HorariosRmtcGoianiaApplication.class.getSimpleName();

    private static HorariosRmtcGoianiaApplication mInstance;

    /**
     * Access the instance of the application Class.
     *
     * @return Application unique instance.
     */
    public static synchronized HorariosRmtcGoianiaApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
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

        // Enable StrictMode
        enableStrictMode();
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

    /**
     * Used to enable {@link StrictMode} during production
     */
    @SuppressWarnings("PointlessBooleanExpression")
    @SuppressLint("NewApi")
    private static void enableStrictMode() {
        if (!BuildConfig.DEBUG || !AndroidUtils.isGingerbreadOrHigher()) {
            return;
        }
        // Enable StrictMode
        final ThreadPolicy.Builder threadPolicyBuilder = new ThreadPolicy.Builder();
        threadPolicyBuilder.detectAll();
        threadPolicyBuilder.penaltyLog();
        setThreadPolicy(threadPolicyBuilder.build());

        // Policy applied to all threads in the virtual machine's process
        final VmPolicy.Builder vmPolicyBuilder = new VmPolicy.Builder();
        vmPolicyBuilder.detectAll();
        vmPolicyBuilder.penaltyLog();
        if (AndroidUtils.isJellyBeanOrHigher()) {
            vmPolicyBuilder.detectLeakedRegistrationObjects();
        }
        setVmPolicy(vmPolicyBuilder.build());
    }
}
