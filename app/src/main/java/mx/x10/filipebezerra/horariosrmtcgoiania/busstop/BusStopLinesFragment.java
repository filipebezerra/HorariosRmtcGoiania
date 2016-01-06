package mx.x10.filipebezerra.horariosrmtcgoiania.busstop;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ArrivalPredictionResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber.ApiSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusStopWithArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * User presentation of {@link BusLine} list.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 02/01/2015
 * @since 3.0.0
 */
public class BusStopLinesFragment extends BaseFragment
        implements BusStopLinesAdapter.OnRequestArrivalPrediction {

    // logging purposes
    private static final String LOG = BusStopLinesFragment.class.getSimpleName();

    // User data handled by this fragment
    private static final String ARG_DATA = "BusStop";

    // State of user data handled by this fragment
    private static final String STATE_ARG_DATA = "State_" + ARG_DATA;

    // Data representation handled by this fragment
    private BusStop mBusStop;

    // View data adapter representation handled by this fragment
    private BusStopLinesAdapter mBusStopLinesAdapter;

    // API subscriber to receive API calls
    private ApiSubscriber<ArrivalPredictionResponse> mApiResponseSubscriber;

    @Bind(R.id.list) protected RecyclerView mBusStopLinesView;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_bus_stop_lines;
    }

    public static BusStopLinesFragment newInstance(@NonNull BusStop data) {
        Timber.d("Creating new instance of %s", LOG);
        BusStopLinesFragment fragment = new BusStopLinesFragment();

        Bundle args = new Bundle();

        Timber.d("Assigning data arguments with value %s", data.toString());
        args.putParcelable(ARG_DATA, data);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() == null)
                || !(getArguments().containsKey(ARG_DATA))) {
            throw new IllegalStateException(
                    "This fragment must contains the argument 'ARG_DATA'.");
        } else if (!(getArguments().get(ARG_DATA) instanceof Parcelable)
                || getArguments().getParcelable(ARG_DATA) == null) {
            throw new IllegalArgumentException(
                    "The argument 'ARG_DATA' must be a android.os.Parcelable instance.");
        }
    }

    @Override
    public void onActivityCreated(Bundle inState) {
        super.onActivityCreated(inState);

        if (inState != null && mBusStop == null) {
            mBusStop = inState.getParcelable(STATE_ARG_DATA);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("Creating view");

        mBusStop = getArguments().getParcelable(ARG_DATA);

        Timber.d("Data retrieved with value %s", mBusStop.toString());

        mBusStopLinesView.setHasFixedSize(true);
        mBusStopLinesView.setAdapter(
                mBusStopLinesAdapter = new BusStopLinesAdapter(getActivity(), mBusStop));
        mBusStopLinesAdapter.setOnRequestArrivalPrediction(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_ARG_DATA, mBusStop);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("Starting fragment");
        BusProvider.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("Pausing fragment");

        if (mApiResponseSubscriber != null) {
            Timber.d("API subscriber is assigned");
            if (mApiResponseSubscriber.isUnsubscribed()) {
                Timber.d("API subscriber is subscribed");
                mApiResponseSubscriber.unsubscribe();
            }

            mApiResponseSubscriber = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("Stopping fragment");
        BusProvider.unregister(this);
    }

    private SubscriberDelegate<ArrivalPredictionResponse> mArrivalPredictionResponseDelegate
            = new SubscriberDelegate<ArrivalPredictionResponse>() {
        @Override
        public void onStart() {
            Timber.d("API subscriber start connection");
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
            Timber.d("API subscriber will emit new data received");
            if (isVisible()) {
                Timber.d("Fragment is visible, continue emitting");

                if (Boolean.parseBoolean(observable.status)) {
                    Timber.d("Response has valid status, continue emitting");

                    if (observable.data.length == 0) {
                        Timber.d("Response hasn't data available, showing feedback message");

                        FeedbackHelper.snackbar(getView(),
                                getString(R.string.feedback_no_arrival_prediction), false);
                    } else {
                        Timber.d("Response with data %s", observable.toString());

                        final ArrivalPrediction arrivalPrediction = ArrivalPrediction
                                .fromModel(observable.data[0]);

                        Timber.d("Response was transformed into data, posting the result %s",
                                arrivalPrediction.toString());

                        BusProvider.post(
                                BusStopWithArrivalPrediction.create(mBusStop, arrivalPrediction));
                    }
                } else {
                    Timber.d("Fragment hasn't valid status, showing feedback: %s",
                            observable.message);
                    FeedbackHelper.snackbar(getView(), observable.message, false);
                }
            } else {
                Timber.d("Fragment is not visible, discarding the response %s",
                        observable.toString());
            }
        }

        @Override
        public void onCompleted() {
            Timber.d("API subscriber completed the emissions");
            if (isVisible()) {
                mMaterialDialogHelper.dismissDialog();
            } else {
                Timber.d("Fragment is not visible, nothing will be done in the view");
            }
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("API subscriber raised error with message %s", e.getMessage());
            if (e.getClass() != null) {
                Timber.d("The cause of the error was %s", e.getCause().getMessage());
            }

            if (isVisible()) {
                mMaterialDialogHelper.dismissDialog();
            } else {
                Timber.d("Fragment is not visible, nothing will be done in the view");
            }
        }
    };

    @Override
    public void onRequest(String busStopId, String busLineNumber) {
        Timber.d("User asks to view the arrival prediction from bus stop %s, line number %s",
                busStopId, busLineNumber);

        if (NetworkUtil.isDeviceConnectedToInternet(getActivity())) {
            Timber.d("Start searching for arrival prediction...");
            mApiResponseSubscriber  = new ApiSubscriber<>(mArrivalPredictionResponseDelegate);

            RetrofitController.instance(getActivity())
                    .searchArrivalPrediction(busStopId, busLineNumber)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mApiResponseSubscriber);
        }else {
            Timber.d("No network connection available, showing feedback message");
            FeedbackHelper.toast(getActivity(), getString(R.string.feedback_no_network), false);
        }
    }
}
