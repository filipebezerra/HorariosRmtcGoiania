package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Classe utilitária responsável por verificar se há conexão com a internet disponível no dispostivo.
 * Por padrão é validado se está conectado à rede móvel ou à uma rede wifi.
 *
 * @author Filipe Bezerra
 * @since 1.4-m1
 */
public final class NetworkUtils {

    public static boolean isConnectingToInternet(final Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

            return (networkInfo != null && networkInfo.isConnected());
        }

        return false;
    }

}
