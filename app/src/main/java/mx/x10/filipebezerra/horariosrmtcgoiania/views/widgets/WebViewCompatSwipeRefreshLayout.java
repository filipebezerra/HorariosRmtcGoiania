package mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Custom Swipe refresh layout supporting webview.
 * <br>
 * Source: http://stackoverflow.com/questions/24658428/swiperefreshlayout-webview-when-scroll-position-is-at-top
 *
 * @author Filipe Bezerra
 * @version 2, 09/03/2015
 * @since #
 */
public class WebViewCompatSwipeRefreshLayout extends SwipeRefreshLayout {
    private CanChildScrollUpCallback mCanChildScrollUpCallback;

    public WebViewCompatSwipeRefreshLayout(final Context context) {
        super(context);
    }

    public WebViewCompatSwipeRefreshLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public interface CanChildScrollUpCallback {
        boolean canSwipeRefreshChildScrollUp();
    }

    public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
        mCanChildScrollUpCallback = canChildScrollUpCallback;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanChildScrollUpCallback != null) {
            return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
        }
        return super.canChildScrollUp();
    }
}
