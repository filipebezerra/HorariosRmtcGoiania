package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * Favorites fragment view.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class FavoritesFragment extends Fragment {
    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
