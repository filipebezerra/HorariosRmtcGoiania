package mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers;

import android.content.Context;
import android.view.View;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AnimationUtils;

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

    public static void show(final Context context, final String message,
            final View... animateViews) {
        show(context, message, false, animateViews);
    }

    public static void show(final Context context, final String message) {
        show(context, message, false, null);
    }

    public static void showSingleLine(final Context context, final String message) {
        show(context, message, true, null);
    }

    public static void showSingleLine(final Context context, final String message,
            final View animateView) {
        show(context, message, true, animateView);
    }

    public static Snackbar build(final Context context, final String message,
            final boolean singleLine, final View... animateViews) {
        final SnackbarType snackbarType = singleLine ? SnackbarType.SINGLE_LINE :
                SnackbarType.MULTI_LINE;

        Snackbar snackbar = Snackbar.with(context);

        if (animateViews != null && animateViews.length > 0) {
            snackbar.eventListener(new EventListener() {
                @Override
                public void onShow(Snackbar snackbar) {
                    for (View view : animateViews)
                        AnimationUtils.moveUp(view, snackbar.getHeight());
                }

                @Override
                public void onDismissed(Snackbar snackbar) {
                    for (View view : animateViews)
                        AnimationUtils.moveDown(view, snackbar.getHeight());
                }

                public void onShowByReplace(Snackbar snackbar) {}
                public void onShown(Snackbar snackbar) {}
                public void onDismissByReplace(Snackbar snackbar) {}
                public void onDismiss(Snackbar snackbar) {}
            });
        }

        return snackbar.type(snackbarType).text(message).swipeToDismiss(true);
    }

    private static void show(final Context context, final String message, final boolean singleLine,
            final View... animateViews) {
        SnackbarManager.show(
                build(context, message, singleLine, animateViews));
    }
}