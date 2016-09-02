package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.favoriteslist;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.fragment.BaseFragment;

/**
 * @author Filipe Bezerra
 */
public class FavoritesListFragment extends BaseFragment {
    public static FavoritesListFragment newInstance() {
        return new FavoritesListFragment();
    }

    @Override
    protected int provideViewResource() {
        return R.layout.favorites_list;
    }
}
