package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.app.Activity;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class ToastHelper {

    private SuperCardToast toast = null;

    public ToastHelper(final Activity activity) {
        toast = new SuperCardToast(activity, SuperToast.Type.STANDARD);
    }

    public void showGeneralAlert(final String message) {
        toast.setAnimations(SuperToast.Animations.FLYIN);
        toast.setDuration(SuperToast.Duration.LONG);
        toast.setBackground(SuperToast.Background.RED);
        toast.setTextSize(SuperToast.TextSize.MEDIUM);
        toast.setSwipeToDismiss(true);
        toast.setTouchToDismiss(true);
        toast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
        toast.setText(message);
        toast.show();
    }

    public void showNoConnectionAlert() {
        showGeneralAlert(toast.getActivity().getResources().getString(R.string.no_connectivity));
    }

}
