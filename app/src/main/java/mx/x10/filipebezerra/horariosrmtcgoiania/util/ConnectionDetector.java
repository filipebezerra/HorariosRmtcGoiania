package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Filipe Bezerra
 * @since 1.4-m1
 */
public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            for (NetworkInfo networkInfo : connectivity.getAllNetworkInfo()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            };
        }

        return false;
    }

}
