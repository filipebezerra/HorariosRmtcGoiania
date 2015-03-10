package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.FavoriteBusStopsAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.FavoriteItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.widget.DividerItemDecoration;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.widget.EmptyRecyclerView;

/**
 * Favorite bus stop list fragment. Loads the context data from database and displays to
 * user interaction and then loading the item selected.
 *
 * @author Filipe Bezerra
 * @version 2.0, 08/03/2015
 * @since #
 */
public class FavoriteBusStopListFragment extends Fragment
        implements FavoriteBusStopsAdapter.OnItemClickListener {

    @NonNull private EmptyRecyclerView mRecyclerView;
    @NonNull private FavoriteBusStopsAdapter mFavoriteBusStopsAdapter;

    public FavoriteBusStopListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.favorite_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(1000);
        mRecyclerView.getItemAnimator().setChangeDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(1000);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(mFavoriteBusStopsAdapter = new FavoriteBusStopsAdapter(
                getFavoritesData()));
        mFavoriteBusStopsAdapter.setOnItemClickListener(this);
    }

    private List<FavoriteBusStop> getFavoritesData() {
        return DaoManager.getInstance(getActivity()).getFavoriteBusStopDao().loadAll();
    }

    @Override
    public void onItemClick(final FavoriteBusStop favoriteBusStop) {
        EventBusProvider.getInstance().getEventBus().post(
                new FavoriteItemSelectionEvent(favoriteBusStop));
    }
}