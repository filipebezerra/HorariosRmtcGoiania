package mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers;

import android.content.Context;
import android.support.annotation.StringRes;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Helper class for showing or building {@link com.afollestad.materialdialogs.MaterialDialog}
 * as a ProgressDialog in Material Design fashion.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class ProgressDialogHelper {
    public static final boolean INDETERMINATE = true;
    public static final int MAX_ZERO = 0;

    /**
     * Shows a basic indeterminate progress dialog within a title and a message.
     *
     * @param context context to show the progress dialog.
     * @param title resource id to progress dialog title.
     * @param message resource id to progress dialog content message.
     */
    public static MaterialDialog show(final Context context, @StringRes final int title,
            @StringRes final int message) {
        MaterialDialog dialog = build(context, title, message);
        dialog.show();
        return dialog;
    }

    /**
     * Creates a basic indeterminate progress dialog within a title and a message and returns to the
     * caller to add callbacks and listeners.
     *
     * @param context context to show the progress dialog.
     * @param title resource id to progress dialog title.
     * @param message resource id to progress dialog content message.
     * @return progress dialog instance.
     */
    public static MaterialDialog build(final Context context, @StringRes final int title,
            @StringRes final int message) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .progress(INDETERMINATE, MAX_ZERO)
                .build();
    }
}
