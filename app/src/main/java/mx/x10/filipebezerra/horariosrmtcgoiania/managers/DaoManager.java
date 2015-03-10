package mx.x10.filipebezerra.horariosrmtcgoiania.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoMaster;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.DaoSession;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 10/03/2015
 * @since #
 */
public class DaoManager {
    private static DaoManager mInstance;
    private final SQLiteDatabase mDatabase;
    private DaoSession mDaoSession;

    private DaoManager(Context context) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(
                context, context.getString(R.string.database_name), null);
        mDatabase = openHelper.getWritableDatabase();
    }
    public static synchronized DaoManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DaoManager(context.getApplicationContext());
        }
        return mInstance;
    }

    private DaoSession requestDaoSession() {
        if (mDaoSession == null) {
            DaoMaster daoMaster = new DaoMaster(mDatabase);
            mDaoSession = daoMaster.newSession();
        }
        return mDaoSession;
    }

    public FavoriteBusStopDao getFavoriteBusStopDao() {
        DaoSession daoSession = requestDaoSession();
        return daoSession.getFavoriteBusStopDao();
    }
}
