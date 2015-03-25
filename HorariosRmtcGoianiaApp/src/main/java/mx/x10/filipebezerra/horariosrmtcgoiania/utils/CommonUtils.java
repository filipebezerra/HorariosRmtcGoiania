package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;

/**
 * Common utility methods.
 *
 * @author Filipe Bezerra
 * @version 2.1, 25/03/2015
 * @since 1.4-m1
 */
public final class CommonUtils {
    /**
     * Checks the network and the wifi state and notifies with a toast to the user.
     *
     * @param context application context.
     * @return if was notified
     */
    public static boolean checkAndNotifyNetworkState(Context context) {
        if (!AndroidUtils.isWifiConnected(context) &&
                !AndroidUtils.isNetworkConnected(context)) {
            SnackBarHelper.show(context,
                    context.getString(R.string.no_internet_connectivity));
            return true;
        } else {
            return false;
        }
    }
}
