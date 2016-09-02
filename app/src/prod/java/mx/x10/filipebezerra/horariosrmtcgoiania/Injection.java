package mx.x10.filipebezerra.horariosrmtcgoiania;

import android.content.Context;
import android.support.annotation.NonNull;
import mx.x10.filipebezerra.horariosrmtcgoiania.data.previsaochegada.PrevisaoChegadaService;
import mx.x10.filipebezerra.horariosrmtcgoiania.data.util.ServiceFactory;

/**
 * @author Filipe Bezerra
 */
public class Injection {
    public static PrevisaoChegadaService providePrevisaoChegadaService(@NonNull Context context) {
        return ServiceFactory.createService(context, PrevisaoChegadaService.class);
    }
}
