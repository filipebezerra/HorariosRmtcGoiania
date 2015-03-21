package mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers;

import android.content.Context;
import android.support.annotation.StringRes;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Helper class for showing or building {@link com.afollestad.materialdialogs.MaterialDialog}
 * as a Dialog in Material Design fashion.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class DialogHelper {
    /**
     * Creates a basic dialog within a title, a message, a text for positive button and a text for
     * negative button.
     *
     * @param context context to show the dialog.
     * @param title resource id to dialog title.
     * @param message resource id to dialog content message.
     * @param positiveText resource id to positive button text.
     * @param negativeText resource id to negative button text.
     */
    public static MaterialDialog show(final Context context, @StringRes final int title,
            @StringRes final int message, @StringRes final int positiveText,
            @StringRes final int negativeText) {
        MaterialDialog.Builder builder = build(context, title, message, positiveText, negativeText);
        return builder.show();
    }

    /**
     *
     * Creates a basic dialog within a title, a message, a text for positive button and a text for
     * negative button and returns the builder to the caller to add callbacks and listeners.
     *
     * @param context context to show the dialog.
     * @param title resource id to dialog title.
     * @param message resource id to dialog content message.
     * @param positiveText resource id to positive button text.
     * @param negativeText resource id to negative button text.
     * @return dialog builder instance.
     */
    public static MaterialDialog.Builder build(final Context context, @StringRes final int title,
            @StringRes final int message, @StringRes final int positiveText,
            @StringRes final int negativeText) {
        return build(context, title, message, positiveText)
                .negativeText(negativeText);
    }

    public static MaterialDialog.Builder build(final Context context, @StringRes final int title,
            @StringRes final int message, @StringRes final int positiveText) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(positiveText);
    }
}