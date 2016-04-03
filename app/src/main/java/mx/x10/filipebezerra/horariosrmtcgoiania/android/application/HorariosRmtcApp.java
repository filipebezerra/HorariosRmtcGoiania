package mx.x10.filipebezerra.horariosrmtcgoiania.android.application;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class HorariosRmtcApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
