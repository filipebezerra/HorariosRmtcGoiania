package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoMaster;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoSession;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class ApplicationSingleton extends Application {

    private static final String LOG_TAG = ApplicationSingleton.class.getSimpleName();

    private static ApplicationSingleton mInstance;
    private DaoSession mDaoSession;
    private RequestQueue mRequestQueue;
    private Bus eventBus;
    private SQLiteDatabase mDatabase;

    /**
     * Access the instance of the application Class.
     *
     * @return Application unique instance.
     */
    public static synchronized ApplicationSingleton getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initDefaultFont();
        setupDatabaseModule();
    }

    /**
     * Setups the default font of this application. Is executed on (@Link #onCreate).
     * The default font is Roboto-Regular.
     */
    private void initDefaultFont() {
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    }

    /**
     * Setups the database of this application. Is executed on (@Link #onCreate).
     */
    private void setupDatabaseModule() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(
                this, getString(R.string.database_name), null);
        mDatabase = openHelper.getWritableDatabase();
    }

    /**
     * Lazy loading Data access object.
     *
     * @return Dao session.
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            DaoMaster daoMaster = new DaoMaster(mDatabase);
            mDaoSession = daoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * Lazy loading request dispatch.
     *
     * @return Request queue.
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
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
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Lazy loading event bus.
     *
     * @return Event bus service.
     */
    public Bus getEventBus() {
        if (eventBus == null) {
            eventBus = new Bus(ThreadEnforcer.MAIN, LOG_TAG);
        }
        return eventBus;
    }

}
