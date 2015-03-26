package mx.x10.filipebezerra.horariosrmtcgoiania.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.CommonUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.adapters.FavoriteBusStopsAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.FavoriteItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets.DividerItemDecoration;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets.EmptyRecyclerView;

/**
 * Favorite bus stop list fragment. Loads the context data from database and displays to
 * user interaction and then loading the item selected.
 *
 * @author Filipe Bezerra
 * @version 2.1, 25/03/2015
 * @since 2.0
 */
public class FavoriteBusStopListFragment extends Fragment
        implements FavoriteBusStopsAdapter.OnItemClickListener {

    @InjectView(R.id.favorite_list) protected EmptyRecyclerView mRecyclerView;

    @InjectView(R.id.empty_view) protected TextView mEmptyView;

    @NonNull private FavoriteBusStopsAdapter mFavoriteBusStopsAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(mEmptyView);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onItemClick(final FavoriteBusStop favoriteBusStop) {
        if (CommonUtils.checkAndNotifyNetworkState(getActivity())) return;

        EventBusProvider.getInstance().getEventBus().post(
                new FavoriteItemSelectionEvent(favoriteBusStop));
    }

    private List<FavoriteBusStop> getFavoritesData() {
        return DaoManager.getInstance(getActivity()).getFavoriteBusStopDao().loadAll();
    }
}