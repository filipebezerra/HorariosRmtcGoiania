package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 08/03/2015
 * @since #
 */
public class FavoriteItemSelectionEvent implements Event<FavoriteBusStop> {
    private FavoriteBusStop mFavoriteBusStop;

    public FavoriteItemSelectionEvent(final FavoriteBusStop favoriteBusStop) {
        mFavoriteBusStop = favoriteBusStop;
    }

    @Override
    public FavoriteBusStop getMessage() {
        return mFavoriteBusStop;
    }

    @Override
    public void setMessage(final FavoriteBusStop message) {
        mFavoriteBusStop = message;
    }
}
