package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.content.Context;
import android.view.View;

import com.gc.materialdesign.widgets.Dialog;

/**
 * Builder for {@link com.gc.materialdesign.widgets.Dialog} in Material Design fashion.
 *
 * @author Filipe Bezerra
 * @version #, 10/03/2015
 * @since #
 */
public class DialogBuilder {

    private final Context mContext;
    private final String mTitle;
    private final String mMesssage;
    private boolean mHasCancelButton;
    private View.OnClickListener mAcceptButtonClickListener;
    private String mAcceptButtonText = "OK";
    private String mCancelButtonText = "CANCELAR";

    public DialogBuilder(Context context, String title, String message) {
        mContext = context;
        mTitle = title;
        mMesssage = message;
    }

    public DialogBuilder addCancelButton() {
        mHasCancelButton = true;
        return this;
    }

    public DialogBuilder addAcceptButtonClickListener(View.OnClickListener listener) {
        mAcceptButtonClickListener = listener;
        return this;
    }

    public DialogBuilder setAcceptButtonText(String text) {
        mAcceptButtonText = text;
        return this;
    }

    public void buildAndShow() {
        Dialog dialog = new Dialog(mContext, mTitle, mMesssage);
        if (mHasCancelButton) {
            dialog.addCancelButton(mCancelButtonText);
        }

        if (mAcceptButtonClickListener != null) {
            dialog.setOnAcceptButtonClickListener(mAcceptButtonClickListener);
        }

        dialog.show();
        dialog.getButtonAccept().setText(mAcceptButtonText);
    }
}