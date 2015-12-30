package mx.x10.filipebezerra.horariosrmtcgoiania.busstop;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.squareup.otto.Subscribe;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ArrivalPredictionResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber.ApiSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.dialog.MaterialDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import org.parceler.Parcels;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Bus stop list visualization.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class BusStopLinesFragment extends BaseFragment
        implements BusStopLinesAdapter.OnRequestArrivalPrediction {

    public static final String ARG_DATA = "BusStop";

    private BusStopLinesAdapter mBusStopLinesAdapter;

    private BusStop mBusStop;

    private MaterialDialogHelper mMaterialDialogHelper;

    private ApiSubscriber<ArrivalPredictionResponse> mApiResponseSubscriber;

    @Bind(R.id.list) protected RecyclerView mBusStopLinesView;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_bus_stop_lines;
    }

    public static BusStopLinesFragment newInstance(@NonNull BusStop data) {
        BusStopLinesFragment fragment = new BusStopLinesFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, Parcels.wrap(data));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBusStop = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));

        mBusStopLinesView.setHasFixedSize(true);
        mBusStopLinesView.setAdapter(
                mBusStopLinesAdapter = new BusStopLinesAdapter(getActivity(), mBusStop));
        mBusStopLinesAdapter.setOnRequestArrivalPrediction(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.register(this);
        mMaterialDialogHelper = MaterialDialogHelper.toContext(getContext());
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
    public void onBusStopFound(GenericEvent<BusStop> event) {
        mBusStop = event.message();

        if (isVisible()) {
            mBusStopLinesAdapter.swapData(mBusStop);
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
                        BusProvider.postOnMain(new GenericEvent<>(arrivalPrediction));
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
    public void onRequest(String busStopId, String busLineNumber) {
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
