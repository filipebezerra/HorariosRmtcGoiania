package mx.x10.filipebezerra.horariosrmtcgoiania.nearby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;

/**
 * Nearby bus stops visualization.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 30/11/2015
 * @since 0.3.0
 */
public class NearbyBusStopsFragment extends BaseFragment implements NearbyBusStopsAdapter.OnRequestAvailableLines {

    public static final String ARG_DATA = "BusStops";

    private NearbyBusStopsAdapter mNearbyBusStopsAdapter;

    private List<BusStop> mBusStopList;

    @Bind(R.id.list) protected RecyclerView mBusStopLinesView;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_nearby_bus_stops;
    }

    public static NearbyBusStopsFragment newInstance(@NonNull List<BusStop> data) {
        NearbyBusStopsFragment fragment = new NearbyBusStopsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, Parcels.wrap(data));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBusStopList = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));

        mBusStopLinesView.setHasFixedSize(true);
        mBusStopLinesView.setAdapter(
                mNearbyBusStopsAdapter = new NearbyBusStopsAdapter(getActivity(), mBusStopList));
        mNearbyBusStopsAdapter.setOnRequestAvailableLines(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.unregister(this);
    }

    @Subscribe
    public void onNearbyBusStopsFound(GenericEvent<List<BusStop>> event) {
        mBusStopList = event.message();

        if (isVisible()) {
            mNearbyBusStopsAdapter.swapData(mBusStopList);
        }
    }

    @Override
    public void onRequest(String busStopId, List<BusLine> lines) {
        if (NetworkUtil.isDeviceConnectedToInternet(getActivity())) {
            BusProvider.post(new GenericEvent<>(Pair.create(busStopId, lines)));
        }else {
            FeedbackHelper.toast(getActivity(), getString(R.string.feedback_no_network), false);
        }
    }
}
