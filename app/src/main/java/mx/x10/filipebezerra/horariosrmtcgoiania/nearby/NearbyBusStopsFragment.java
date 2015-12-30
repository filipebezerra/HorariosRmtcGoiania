package mx.x10.filipebezerra.horariosrmtcgoiania.nearby;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ArrivalPredictionResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber.ApiSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    private ApiSubscriber<ArrivalPredictionResponse> mApiResponseSubscriber;

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
    public void onPause() {
        super.onPause();

        if (mApiResponseSubscriber != null) {
            if (mApiResponseSubscriber.isUnsubscribed()) {
                mApiResponseSubscriber.unsubscribe();
            }

            mApiResponseSubscriber = null;
        }
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

    private SubscriberDelegate<ArrivalPredictionResponse> mArrivalPredictionResponseDelegate
            = new SubscriberDelegate<ArrivalPredictionResponse>() {
        @Override
        public void onStart() {
            mMaterialDialogHelper
                    .showIndeterminateProgress(getString(
                                    R.string.feedback_searching_arrival_prediction),
                            true,
                            dialog -> {
                                mMaterialDialogHelper.dismissDialog();
                                mApiResponseSubscriber.unsubscribe();
                            });
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onNext(ArrivalPredictionResponse observable) {
            if (isVisible()) {
                if (Boolean.parseBoolean(observable.status)) {
                    if (observable.data.length == 0) {
                        FeedbackHelper.snackbar(getView(),
                                getString(R.string.feedback_no_arrival_prediction), false);
                    } else {
                        final ArrivalPrediction arrivalPrediction = ArrivalPrediction
                                .fromModel(observable.data[0]);
                        BusProvider.post(new GenericEvent<>(arrivalPrediction));
                    }
                } else {
                    FeedbackHelper.snackbar(getView(), observable.message, false);
                }
            }
        }

        @Override
        public void onCompleted() {
            if (isVisible()) {
                mMaterialDialogHelper.dismissDialog();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isVisible()) {
                mMaterialDialogHelper.dismissDialog();
            }
        }
    };

    @Override
    public void onRequest(String busStopId, List<BusLine> lines) {
        if (NetworkUtil.isDeviceConnectedToInternet(getActivity())) {
            String [] list = new String[lines.size()];
            for (int i = 0; i < lines.size() ; i++) {
                final BusLine line = lines.get(i);
                list[i] = line.getNumber() + " - " + line.getItinerary();
            }

            mMaterialDialogHelper.showListDialog(getContext(), "Linhas Disponíveis", -1,
                    (dialog, itemView, which, text) -> {
                        requestArrivalPrediction(busStopId, lines.get(0).getNumber());
                        return true;
                    }, list);
        }else {
            FeedbackHelper.toast(getActivity(), getString(R.string.feedback_no_network), false);
        }
    }

    private void requestArrivalPrediction(String busStopId, String busLineNumber) {
        if (NetworkUtil.isDeviceConnectedToInternet(getActivity())) {
            mApiResponseSubscriber  = new ApiSubscriber<>(mArrivalPredictionResponseDelegate);

            RetrofitController.instance(getActivity())
                    .searchArrivalPrediction(busStopId, busLineNumber)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mApiResponseSubscriber);
        }else {
            FeedbackHelper.toast(getActivity(), getString(R.string.feedback_no_network), false);
        }
    }
}
