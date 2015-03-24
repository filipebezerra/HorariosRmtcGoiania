package mx.x10.filipebezerra.horariosrmtcgoiania.managers;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author Filipe Bezerra 
 * @version 2.0
 * @since 2.0_25/02/2015 
 */
public class RequestQueueManager {

    private static final String LOG_TAG = RequestQueueManager.class.getSimpleName();
    
    private static RequestQueueManager mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;
    
    private RequestQueueManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Lazy loading request dispatch.
     *
     * @return Request queue.
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized RequestQueueManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueManager(context);
        }
        return mInstance;
    }

    /**
     * Adds a new request to queue. This request is identified by the tag passed in.
     *
     * @param req Specific Request to the service.
     * @param tag Identifier for this request.
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds a new request to queue. This method will use the internal LOG_TAG identifier
     * which is the simple class name.
     *
     * @param req Specific Request to the service.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(LOG_TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests made for a specific tag.
     *
     * @param tag Request identifier.
     */
    public void cancelPendingRequests(Object tag) {
        getRequestQueue().cancelAll(tag);
    }
    
}
