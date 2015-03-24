package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.view.View;
import android.view.animation.LinearInterpolator;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * Utility class for viewing animations.
 *
 * @author Filipe Bezerra
 * @version #, 20/03/2015
 * @since #
 */
public class AnimationUtils {
    /**
     * This method will cause the View's <code>translationY</code> property to be animated by the
     * specified value as negative.
     *
     * @param view View to animate.
     * @param value The amount to be animated by, as an offset from the current value.
     */
    public static void moveUp(final View view, final float value) {
        animate(view).setInterpolator(new LinearInterpolator()).translationYBy(-value).start();
    }

    /**
     * This method will cause the View's <code>translationY</code> property to be animated by the
     * specified value as positive.
     *
     * @param view View to animate.
     * @param value The amount to be animated by, as an offset from the current value.
     */
    public static void moveDown(final View view, final float value) {
        animate(view).setInterpolator(new LinearInterpolator()).translationYBy(value).start();
    }
}
