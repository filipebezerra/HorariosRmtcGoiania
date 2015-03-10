package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.content.Context;

import com.gc.materialdesign.widgets.ProgressDialog;

/**
 * Helper class for showing {@link com.gc.materialdesign.widgets.ProgressDialog} in Material
 * Design fashion.
 *
 * @author Filipe Bezerra
 * @version 2.0, 10/03/2015
 * @since #
 */
public class ProgressDialogHelper {
    private static ProgressDialog mProgressDialog;

    /**
     * Shows the progress dialog. Use {@link #dismiss()} to hides this.
     *
     * @param context the context.
     * @param progressTitle title to show in this dialog.
     * @param progressColor progress color to show in this dialog.
     */
    public static void show(final Context context, final String progressTitle, final int progressColor) {
        mProgressDialog = new ProgressDialog(context, progressTitle, progressColor);
        mProgressDialog.show();
        mProgressDialog.getTitleTextView().setTextSize(16);
    }

    /**
     * Hides the current dialog if is showing.
     */
    public static void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
