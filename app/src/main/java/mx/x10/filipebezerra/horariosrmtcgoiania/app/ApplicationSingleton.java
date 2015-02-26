package mx.x10.filipebezerra.horariosrmtcgoiania.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

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

}
