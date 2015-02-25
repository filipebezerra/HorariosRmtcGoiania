package mx.x10.filipebezerra.horariosrmtcgoiania.event;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

/**
 *
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_24/02/2015
 */
public class FavoriteBusStopPersistenceEvent {

    public enum PersistenceOperationType {
        INSERTION, DELETION
    }

    private FavoriteBusStop mFavoriteBusStop;

    private PersistenceOperationType mPersistenceOperationType;

    public FavoriteBusStopPersistenceEvent(FavoriteBusStop favoriteBusStop,
                                           PersistenceOperationType persistenceOperationType) {
        mFavoriteBusStop = favoriteBusStop;
        mPersistenceOperationType = persistenceOperationType;
    }

    public FavoriteBusStop getFavoriteBusStop() {
        return mFavoriteBusStop;
    }

    public void setFavoriteBusStop(FavoriteBusStop favoriteBusStop) {
        mFavoriteBusStop = favoriteBusStop;
    }

    public PersistenceOperationType getPersistenceOperationType() {
        return mPersistenceOperationType;
    }

    public void setPersistenceOperationType(PersistenceOperationType persistenceOperationType) {
        mPersistenceOperationType = persistenceOperationType;
    }

}
