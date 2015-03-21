package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

/**
 * A helper class for showing a notification with SnackBar style.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class SnackBarHelper {
    public static void show(Snackbar snackbar) {
        SnackbarManager.show(snackbar);
    }

    public static void show(final Context context, final String message) {
        show(context, message, false);
    }

    public static void showSingleLine(final Context context, final String message) {
        show(context, message, true);
    }

    public static Snackbar build(final Context context, final String message,
            final boolean singleLine) {
        final SnackbarType snackbarType = singleLine ? SnackbarType.SINGLE_LINE :
                SnackbarType.MULTI_LINE;

        return Snackbar.with(context)
                .type(snackbarType)
                .text(message)
                .swipeToDismiss(true);
    }

    private static void show(final Context context, final String message, final boolean singleLine) {
        SnackbarManager.show(
                build(context, message, singleLine));
    }
}
