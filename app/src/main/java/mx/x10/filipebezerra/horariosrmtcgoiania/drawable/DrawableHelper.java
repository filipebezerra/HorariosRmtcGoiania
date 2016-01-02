package mx.x10.filipebezerra.horariosrmtcgoiania.drawable;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

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
        // TODO: Bug (https://code.google.com/p/android/issues/detail?id=198082)
        // Another approach: PorterDuff.Mode.SRC_ATOP
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    }

    public static void tint(@NonNull Context context, @ColorRes int colorRes,
            @NonNull Drawable... drawables) {
        for (Drawable drawable : drawables) {
            tint(context, colorRes, drawable);
        }
    }

    public static void changeLeftCompoundDrawable(@NonNull Context context,
            @NonNull TextView textView, @DrawableRes int drawableRes) {
        final Drawable newDrawable = ContextCompat.getDrawable(context, drawableRes);

        final Drawable[] actualDrawables = textView.getCompoundDrawables();
        textView.setCompoundDrawables(newDrawable,
                actualDrawables[Drawables.TOP],
                actualDrawables[Drawables.RIGHT],
                actualDrawables[Drawables.BOTTOM]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelative(newDrawable,
                    actualDrawables[Drawables.TOP],
                    actualDrawables[Drawables.RIGHT],
                    actualDrawables[Drawables.BOTTOM]);
        }
    }

    public static void changeLeftDrawableColor(@NonNull Context context,
            @NonNull TextView textView, @ColorRes int colorRes) {
        final Drawable leftDrawable = drawableAt(textView, Drawables.LEFT);

        if (leftDrawable != null) {
            DrawableHelper.tint(context, colorRes, leftDrawable);
        }

        final Drawable leftRelativeDrawable = relativeDrawableAt(textView, Drawables.LEFT);

        if (leftRelativeDrawable != null) {
            DrawableHelper.tint(context, colorRes, leftRelativeDrawable);
        }
    }

    private static Drawable drawableAt(TextView textView,
            @IntRange(from = 0, to = 3) int position) {
        return textView.getCompoundDrawables()[position];
    }

    private static Drawable relativeDrawableAt(TextView textView,
            @IntRange(from = 0, to = 3) int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return textView.getCompoundDrawablesRelative()[position];
        } else {
            return null;
        }
    }

    private static class Drawables {
        static final int LEFT = 0;
        static final int TOP = 1;
        static final int RIGHT = 2;
        static final int BOTTOM = 3;
    }
}
