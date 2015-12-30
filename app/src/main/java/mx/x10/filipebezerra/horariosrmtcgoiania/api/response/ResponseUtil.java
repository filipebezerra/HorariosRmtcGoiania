package mx.x10.filipebezerra.horariosrmtcgoiania.api.response;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * API response utilities.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class ResponseUtil {
    public static Gson sGson = new GsonBuilder().setPrettyPrinting().create();

    public static String toPrintable(@NonNull Object o) {
        return sGson.toJson(o);
    }
}
