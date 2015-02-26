package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.otto.Subscribe;

import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.FavoriteBusStopsAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.PersistenceEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;

/**
 * Right drawer fragment composed for the favorites (@Link FavoriteBusStop) which are persistence
 * objects.
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_23/02/2015
 */
public class RightDrawerFragment extends BaseDrawerSideFragment {

    private FavoriteBusStopsAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_drawer, container, false);

        List<FavoriteBusStop> mFavoriteData = ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao().loadAll();
        mListAdapter = new FavoriteBusStopsAdapter(getActivity(), R.layout.navdrawer_right_items,
                mFavoriteData);
        setListAdapter(mListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(RightDrawerFragment.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        FavoriteBusStop favoriteBusStop = mListAdapter.getItem(position);
        Bundle arguments = new Bundle();
        arguments.putString(HorarioViagemFragment.ARG_PARAM_BUS_STOP_NUMBER,
                String.valueOf(favoriteBusStop.getStopCode()));
        
        postDrawerItemSelectionEvent(new DrawerItemSelectionEvent(new DrawerItemSelectionMessage(
                LeftDrawerFragment.getDrawerItemHorariosViagem(), arguments)));
    }
    
    @Subscribe
    public void onPersistenceEvent(PersistenceEvent event) {
        switch (event.getMessage().getPersistenceType())  {
            case INSERTION:
                mListAdapter.add((FavoriteBusStop) event.getMessage().getEntity());
                break;
            case DELETION:
                mListAdapter.remove((FavoriteBusStop) event.getMessage().getEntity());
                break;
        }

        // TODO : Update the counter in the BadgeView
    }

}