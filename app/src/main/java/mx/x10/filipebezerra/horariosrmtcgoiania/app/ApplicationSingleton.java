package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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

    private static final String TAG = ApplicationSingleton.class.getSimpleName();

    private static ApplicationSingleton mInstance;
    private DaoSession mDaoSession;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
        mInstance = this;
        setupDatabaseModule();
    }

    private void setupDatabaseModule() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(
                this, getString(R.string.database_name), null);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static ApplicationSingleton getInstance() {
        return mInstance;
    }

    /**
     * Lazy loading
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
