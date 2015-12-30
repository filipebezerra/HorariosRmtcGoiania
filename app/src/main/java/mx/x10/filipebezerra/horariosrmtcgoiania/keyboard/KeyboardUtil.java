package mx.x10.filipebezerra.horariosrmtcgoiania.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Keyboard utilities.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 01/12/2015
 * @since 0.1.0
 */
public class KeyboardUtil {
    private KeyboardUtil() {
        // no instances
    }

    public static void focusThenShowKeyboard(@NonNull Context context, @NonNull View view) {
        if (view.isShown() && view.isFocusable()) {
            if (view.requestFocus()) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                        INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view.findFocus(), 0);
            }
        }
    }
}
