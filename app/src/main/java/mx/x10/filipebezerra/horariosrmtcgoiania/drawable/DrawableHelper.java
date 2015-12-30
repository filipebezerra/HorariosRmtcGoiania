package mx.x10.filipebezerra.horariosrmtcgoiania.drawable;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Drawable tinting helper.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 07/12/2015
 * @since 0.1.0
 */
public class DrawableHelper {
    private DrawableHelper() {
        // singleton
    }

    public static void tint(@NonNull Context context, @ColorRes int colorRes,
                            @NonNull Drawable drawable) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
    }

    public static void tint(@NonNull Context context, @ColorRes int colorRes,
                            @NonNull Drawable... drawables) {
        for(Drawable drawable : drawables) {
            tint(context, colorRes, drawable);
        }
    }
}
