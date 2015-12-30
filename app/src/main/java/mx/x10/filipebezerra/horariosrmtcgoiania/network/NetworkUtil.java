package mx.x10.filipebezerra.horariosrmtcgoiania.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.*;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Network utilities.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 26/11/2015
 * @since 0.1.0
 */
public class NetworkUtil {
    private NetworkUtil() {
        // no instances
    }

    @SuppressWarnings("deprecation")
    // in the future see: http://developer.android.com/intl/pt-br/reference/android/support/v4/net/ConnectivityManagerCompat.html
    public static boolean isDeviceConnectedToInternet(@NonNull Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
                CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP) {
            for (Network network : connManager.getAllNetworks()) {
                if (network != null) {
                    final NetworkInfo networkInfo = connManager.getNetworkInfo(network);

                    if (networkInfo != null && networkInfo.isConnected()) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            NetworkInfo mWifi = connManager.getNetworkInfo(TYPE_WIFI);
            if (mWifi != null && mWifi.isConnected()) {
                return true;
            }

            NetworkInfo m3G = connManager.getNetworkInfo(TYPE_MOBILE);
            if (m3G != null && m3G.isConnected()) {
                return true;
            }

            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.HONEYCOMB_MR2) {
                NetworkInfo mEthernet = connManager.getNetworkInfo(TYPE_ETHERNET);
                return mEthernet != null && mEthernet.isConnected();
            } else {
                return false;
            }
        }
    }
}
