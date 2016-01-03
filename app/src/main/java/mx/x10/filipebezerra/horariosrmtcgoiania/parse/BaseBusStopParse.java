package mx.x10.filipebezerra.horariosrmtcgoiania.parse;

import com.parse.ParseObject;
import org.json.JSONArray;

/**
 * Base implementation to reuse in {@link BusStopParse} and {@link BusTerminalParse}.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.03
 */
public abstract class BaseBusStopParse extends ParseObject {
    public static final String KEY_ENDERECO = "endereco";
    public static final String KEY_ID_PONTO_PARADA = "idPontoParada";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LINHAS = "linhas";

    public BaseBusStopParse() {
        super();
    }

    public String getEndereco() {
        return getString(KEY_ENDERECO);
    }

    public String getIdPontoParada() {
        return getString(KEY_ID_PONTO_PARADA);
    }

    public String getLatitude() {
        return getString(KEY_LATITUDE);
    }

    public String getLongitude() {
        return getString(KEY_LONGITUDE);
    }

    public JSONArray getLinhas() {
        return getJSONArray(KEY_LINHAS);
    }

    public static class BusLine {
        public String numero;
        public String itinerario;
    }
}
