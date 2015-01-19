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
        prepareCommonConfiguration();
    }
    
    private void prepareCommonConfiguration() {
        toast.setAnimations(SuperToast.Animations.FLYIN);
        toast.setDuration(SuperToast.Duration.LONG);
        toast.setTextSize(SuperToast.TextSize.MEDIUM);
        toast.setSwipeToDismiss(true);
        toast.setTouchToDismiss(true);
    }
    
    private void show(final String text) {
        toast.setText(text);
        toast.show();
    }

    public void showError(final String error) {
        toast.setBackground(SuperToast.Background.RED);
        toast.setIcon(SuperToast.Icon.Dark.EXIT, SuperToast.IconPosition.LEFT);
        show(error);
    }
    
    public void showWarning(final String warning) {
        toast.setBackground(SuperToast.Background.ORANGE);
        toast.setIcon(SuperToast.Icon.Dark.REDO, SuperToast.IconPosition.LEFT);
        show(warning);
    }
    
    public void showInformation(final String info) {
        toast.setBackground(SuperToast.Background.BLUE);
        toast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
        show(info);
    }

}
