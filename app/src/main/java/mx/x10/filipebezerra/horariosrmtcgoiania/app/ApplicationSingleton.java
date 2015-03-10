package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.makeLogTag;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class ApplicationSingleton extends Application {
    private static final String LOG_TAG = makeLogTag(ApplicationSingleton.class);

    private static ApplicationSingleton mInstance;

    /**
     * Access the instance of the application Class.
     *
     * @return Application unique instance.
     */
    public static synchronized ApplicationSingleton getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initDefaultFont();
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
