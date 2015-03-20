package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * A helper class for showing a notification with SnackBar style.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class SnackBarHelper {

    public static final int NO_ACTION_LABEL_RES_ID = 0;
    public static final int NO_ACTION_COLOR_RES_ID = 0;

    public static void show(final Context context, final String message) {
        show(context, message, false);
    }

    public static void showSingleLine(final Context context, final String message) {
        show(context, message, true);
    }

    private static void show(final Context context, final String message, final boolean singleLine) {
        show(context, message, singleLine, NO_ACTION_LABEL_RES_ID, NO_ACTION_COLOR_RES_ID, null);
    }

    private static void show(final Context context, final String message, final boolean singleLine,
            @StringRes final int actionLabelResId, @ColorRes final int actionColorResId,
            final ActionClickListener actionListener) {

        final SnackbarType snackbarType = singleLine ? SnackbarType.SINGLE_LINE :
                SnackbarType.MULTI_LINE;

        Snackbar snackbar = Snackbar.with(context);

        if (actionListener != null && snackbarType != SnackbarType.MULTI_LINE) {
            snackbar.actionColorResource(actionColorResId)
                    .actionLabel(actionLabelResId)
                    .actionListener(actionListener);
        }

        SnackbarManager.show(
                snackbar.type(snackbarType)
                        .text(message)
                        .swipeToDismiss(true));
    }
}
