package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

import static android.support.design.widget.Snackbar.make;
import static android.widget.Toast.makeText;

/**
 * @author Filipe Bezerra
 */
public class FeedbackHelper {
    public static void toast(@NonNull Context inContext, @NonNull String message,
            boolean showQuickly) {
        makeText(inContext, message, showQuickly ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();
    }

    private static Snackbar getSnackbar(@NonNull View inView, @NonNull CharSequence message,
            boolean showQuickly) {
        final Snackbar snackbar = make(inView, message,
                showQuickly ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG);
        final View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(ContextCompat.getColor(inView.getContext(),
                R.color.color_accent));

        final TextView textView = (TextView) snackbarView.findViewById(
                android.support.design.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColor(inView.getContext(), android.R.color.white));

        return snackbar;
    }

    public static void snackbar(@NonNull View inView, @NonNull CharSequence message) {
        getSnackbar(inView, message, false)
                .show();
    }

    public static void snackbar(@NonNull View inView, @NonNull CharSequence message,
            boolean showQuickly) {

        getSnackbar(inView, message, showQuickly)
                .show();
    }

    public static void snackbar(@NonNull View inView, @NonNull CharSequence message,
            boolean showQuickly, @NonNull Snackbar.Callback callback) {

        getSnackbar(inView, message, showQuickly)
                .setCallback(callback)
                .show();
    }

    public static void snackbar(@NonNull View inView, @NonNull CharSequence message,
            boolean showQuickly, @NonNull String actionText,
            @NonNull View.OnClickListener actionListener) {

        getSnackbar(inView, message, showQuickly)
                .setAction(actionText, actionListener)
                .show();
    }
}
