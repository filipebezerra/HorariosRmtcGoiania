package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ConstantsUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application implements ConstantsUtils {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(DEFAULT_TYPEFACE, R.attr.fontPath);
        LogUtils.log(LogUtils.LogType.DEBUG, "Configuração da fonte padrão do aplicativo aplicada.");
    }

}
