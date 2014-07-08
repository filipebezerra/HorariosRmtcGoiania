package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.util.Log;

import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;

/**
 * @author Filipe Bezerra
 * @since 1.0
 */
public final class Operations implements Constants {
    public static void log(final String logMessage) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, logMessage);
        }
    }
}
