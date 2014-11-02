package mx.x10.filipebezerra.horariosrmtcgoiania;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    }

}
