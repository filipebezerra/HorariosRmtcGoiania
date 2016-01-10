package mx.x10.filipebezerra.horariosrmtcgoiania.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.activities.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AndroidUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.adapters.FavoriteBusStopsAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.FavoriteItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets.DividerItemDecoration;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets.EmptyRecyclerView;

/**
 * Favorite bus stop list fragment. Loads the context data from database and displays to
 * user interaction and then loading the item selected.
 *
 * @author Filipe Bezerra
 * @version 2.3, 09/01/2016
 * @since 2.0
 */
public class FavoriteBusStopListFragment extends Fragment
        implements FavoriteBusStopsAdapter.OnItemClickListener {

    @Bind(R.id.favorite_list) protected EmptyRecyclerView mRecyclerView;

    @Bind(R.id.empty_view) protected LinearLayout mEmptyView;

    @NonNull private FavoriteBusStopsAdapter mFavoriteBusStopsAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(1000);
        mRecyclerView.getItemAnimator().setChangeDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(1000);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.abc_list_divider_mtrl_alpha)));

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(mFavoriteBusStopsAdapter = new FavoriteBusStopsAdapter(
                getFavoritesData()));
        mFavoriteBusStopsAdapter.setOnItemClickListener(this);
        ((BaseActivity)getActivity()).showFabMenu();
    }

    @Override
    public void onResume() {
        super.onResume();

        final long count = DaoManager.getInstance(getContext())
                .getFavoriteBusStopDao().count();

        if (count == 0) {
            mFavoriteBusStopsAdapter.clear();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(final FavoriteBusStop favoriteBusStop) {
        FragmentActivity drawerActivity = getActivity();
        if (AndroidUtils.checkAndNotifyNetworkState(drawerActivity,
                ((BaseActivity) drawerActivity).getFabMenu())) return;

        EventBusProvider.getInstance().getEventBus().post(
                new FavoriteItemSelectionEvent(favoriteBusStop));
    }

    private List<FavoriteBusStop> getFavoritesData() {
        return DaoManager.getInstance(getActivity()).getFavoriteBusStopDao().loadAll();
    }
}