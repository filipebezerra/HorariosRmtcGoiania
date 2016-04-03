package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Bind;
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
    @Bind(R.id.empty_state) LinearLayout mEmptyStateView;
    @Bind(android.R.id.list) RecyclerView mRecyclerView;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmptyStateView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
