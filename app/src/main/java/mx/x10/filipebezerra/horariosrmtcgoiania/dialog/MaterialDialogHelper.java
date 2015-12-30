package mx.x10.filipebezerra.horariosrmtcgoiania.dialog;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * {@link com.afollestad.materialdialogs.MaterialDialog} helper class.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class MaterialDialogHelper {
    private static MaterialDialog sMaterialDialog;

    @NonNull private final Context mContext;

    private MaterialDialogHelper(@NonNull Context context) {
        mContext = context;
    }

    public static MaterialDialogHelper toContext(@NonNull Context context) {
        return new MaterialDialogHelper(context);
    }

    public void showIndeterminateProgress(@NonNull String content, boolean cancelable,
            @Nullable OnCancelListener cancelListener) {

        sMaterialDialog = new MaterialDialog.Builder(mContext)
                .content(content)
                .progress(true, 0)
                .cancelable(cancelable)
                .build();

        if (cancelListener != null) {
            sMaterialDialog.setOnCancelListener(cancelListener);
        }

        sMaterialDialog.show();
    }

    public void dismissDialog() {
        if (sMaterialDialog != null && sMaterialDialog.isShowing()) {
            sMaterialDialog.dismiss();
        }
    }
}
