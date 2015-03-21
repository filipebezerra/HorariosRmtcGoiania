package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/25/2015
 * @since #
 */
public class SQLitePersistenceMessage implements PersistenceMessage<FavoriteBusStop> {

    protected PersistenceType mPersistenceType;

    protected FavoriteBusStop mEntity;

    public SQLitePersistenceMessage() {
    }

    public SQLitePersistenceMessage(PersistenceType persistenceType, FavoriteBusStop
            entity) {
        mPersistenceType = persistenceType;
        mEntity = entity;
    }

    @Override
    public PersistenceType getPersistenceType() {
        return mPersistenceType;
    }

    @Override
    public void setPersistenceType(PersistenceType type) {
        mPersistenceType = type;
    }

    @Override
    public FavoriteBusStop getEntity() {
        return mEntity;
    }

    @Override
    public void setEntity(FavoriteBusStop entity) {
        mEntity = entity;
    }

}
