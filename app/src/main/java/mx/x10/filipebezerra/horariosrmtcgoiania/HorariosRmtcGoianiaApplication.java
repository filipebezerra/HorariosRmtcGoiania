package mx.x10.filipebezerra.horariosrmtcgoiania;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoMaster;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoSession;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Classe da aplicaçao responsavel por definir configurar globais do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public class HorariosRmtcGoianiaApplication extends Application {

    private static HorariosRmtcGoianiaApplication instance;

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
        instance = this;
        setupDatabaseModule();
    }

    private void setupDatabaseModule() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(
                this, getString(R.string.database_name), null);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
