package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.app.Activity;

import com.gc.materialdesign.widgets.SnackBar;

/**
 * A helper class for showing a notification with SnackBar style.
 *
 * @author Filipe Bezerra
 * @version 2.0, 26/02/2015
 * @since #
 */
public class SnackBarHelper {
    
    public static void show(Activity activity, String message) {
        SnackBar snackbar = new SnackBar(activity, message, null, null);
        snackbar.show();
    }
    
}
