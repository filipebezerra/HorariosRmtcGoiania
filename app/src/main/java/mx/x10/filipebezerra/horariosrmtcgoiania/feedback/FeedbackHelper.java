package mx.x10.filipebezerra.horariosrmtcgoiania.feedback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

import static android.support.design.widget.Snackbar.make;
import static android.support.v4.content.ContextCompat.getColor;
import static android.widget.Toast.makeText;

/**
 * Helper class for displaying feedback messages using the {@link android.widget.Toast} API,
 * {@link android.support.design.widget.Snackbar} API.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 26/11/2015
 * @since 0.1.0
 */
public class FeedbackHelper {
    public static void toast(@NonNull Context inContext, @NonNull String message,
        boolean showQuickly) {
        makeText(inContext, message, showQuickly ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }

    private static Snackbar getSnackbar(@NonNull View inView, @NonNull String message,
        boolean showQuickly) {
        final Snackbar snackbar = make(inView, message,
                showQuickly ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG);
        final View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getColor(inView.getContext(), R.color.brown_900));

        final TextView textView = (TextView) snackbarView.findViewById(
                android.support.design.R.id.snackbar_text);

        textView.setTextColor(getColor(inView.getContext(), R.color.white));

        return snackbar;
    }

    public static void snackbar(@NonNull View inView, @NonNull String message,
        boolean showQuickly) {

        getSnackbar(inView, message, showQuickly)
                .show();
    }

    public static void snackbar(@NonNull View inView, @NonNull String message,
        boolean showQuickly, @NonNull Snackbar.Callback callback) {

        getSnackbar(inView, message, showQuickly)
                .setCallback(callback)
                .show();
    }

    public static void snackbar(@NonNull View inView, @NonNull String message,
        boolean showQuickly, @NonNull String actionText,
        @NonNull OnClickListener actionListener) {

        getSnackbar(inView, message, showQuickly)
                .setAction(actionText, actionListener)
                .show();
    }
}
