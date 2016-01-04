package mx.x10.filipebezerra.horariosrmtcgoiania.nearby;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ArrivalPredictionResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.NearbyBusStopsResponse;
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
import mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_RESOLVE_ERROR;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.PlayServicesPermission.FINE_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.STATE_LAST_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.STATE_RESOLVING_ERROR;

/**
 * Nearby bus stops visualization.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 30/11/2015
 * @since 0.3.0
 */
public class NearbyBusStopsFragment extends BaseFragment
        implements NearbyBusStopsAdapter.OnRequestAvailableLines,
        PlayServicesHelper.PlayServicesCallbacks {

    private static final String LOG = NearbyBusStopsFragment.class.getSimpleName();

    private static final String DIALOG_ERROR = "dialog_error";

    // State of user data handled by this fragment
    private static final String STATE_DATA = "State_Data";

    private NearbyBusStopsAdapter mNearbyBusStopsAdapter;

    private List<BusStop> mBusStopList;

    private ApiSubscriber<ArrivalPredictionResponse> mApiResponseSubscriber;

    private ApiSubscriber<NearbyBusStopsResponse> mNearbyBusStopsApiSubscriber;

    private PlayServicesHelper mPlayServicesHelper;

    @Bind(R.id.list) protected RecyclerView mBusStopLinesView;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_nearby_bus_stops;
    }

    public static NearbyBusStopsFragment newInstance() {
        Timber.d("Creating new instance of %s", LOG);
        return new NearbyBusStopsFragment();
    }

    @Override
    public void onActivityCreated(Bundle inState) {
        super.onActivityCreated(inState);
        mPlayServicesHelper = new PlayServicesHelper(getActivity(), inState, this);

        if (inState != null && mBusStopList == null) {
            mBusStopList = inState.getParcelableArrayList(STATE_DATA);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("Starting fragment");
        BusProvider.register(this);
        mPlayServicesHelper.connect();
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
        mPlayServicesHelper.disconnect();

        if (mNearbyBusStopsApiSubscriber != null) {
            if (!mNearbyBusStopsApiSubscriber.isUnsubscribed()) {
                mNearbyBusStopsApiSubscriber.unsubscribe();
            }

            mNearbyBusStopsApiSubscriber = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mPlayServicesHelper.isResolvingError());
        outState.putParcelable(STATE_LAST_LOCATION, mPlayServicesHelper.getLastLocation());

        if (mBusStopList != null) {
            outState.putParcelableArrayList(STATE_DATA, new ArrayList<>(mBusStopList));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                mPlayServicesHelper.setRequestedPermissionGranted(FINE_LOCATION);
            } else {
                // Permission was denied or request was cancelled
                Timber.i("%s Permission was denied or request was cancelled",
                        permissions[0]);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            boolean isErrorSolved = resultCode == Activity.RESULT_OK;
            Timber.i("Connection error was solved = %b", isErrorSolved);
            mPlayServicesHelper.setResolvingError(isErrorSolved);
        }
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

                        BusProvider.post(new GenericEvent<>(arrivalPrediction));
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
    public void onRequest(String busStopId, List<BusLine> lines) {
        Timber.d("User asks to view the available lines of bus stop %s",
                busStopId);
        Timber.d("The original data containing the available lines is %s",
                lines.toString());

        String [] list = new String[lines.size()];
        for (int i = 0; i < lines.size() ; i++) {
            final BusLine line = lines.get(i);
            list[i] = line.getNumber() + " - " + line.getItinerary();
        }

        Timber.d("Available lines was transformed into %s", Arrays.asList(list));

        mMaterialDialogHelper.showListDialog(getContext(), "Linhas Disponíveis", -1,
                (dialog, itemView, which, text) -> {
                    final BusLine busLine = lines.get(which);
                    Timber.d("User selected the item %d: %s", which, busLine);
                    requestArrivalPrediction(busStopId, busLine.getNumber());
                    return true;
                }, list);
    }

    private void requestArrivalPrediction(String busStopId, String busLineNumber) {
        if (NetworkUtil.isDeviceConnectedToInternet(getActivity())) {
            Timber.d("Start searching for arrival prediction from bus stop %s, bus line %s...",
                busStopId, busLineNumber);
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

    private SubscriberDelegate<NearbyBusStopsResponse> mNearbyBusStopsResponseRxDelegate
            = new SubscriberDelegate<NearbyBusStopsResponse>() {
        @Override
        public void onStart() {
            mMaterialDialogHelper
                    .showIndeterminateProgress(
                            getString(R.string.feedback_searching_nearby_bus_stop),
                            true,
                            dialog -> {
                                mMaterialDialogHelper.dismissDialog();
                                mNearbyBusStopsApiSubscriber.unsubscribe();
                            });
        }

        @Override
        public void onNext(NearbyBusStopsResponse observable) {
            if (Boolean.parseBoolean(observable.status)) {
                final List<BusStopModel> model = observable.data;

                if (model.isEmpty()) {
                    // TODO Apply empty state strategy
                } else {
                    switch (model.size()) {
                        case 1:
                            final BusStop busStop = BusStop.fromModel(model.get(0));
                            BusProvider.post(busStop);

                            break;

                        default:
                            mBusStopList = BusStop.fromModel(model);

                            mBusStopLinesView.setHasFixedSize(true);
                            mBusStopLinesView.setAdapter(
                                    mNearbyBusStopsAdapter = new NearbyBusStopsAdapter(
                                            getActivity(), mBusStopList));
                            mNearbyBusStopsAdapter.setOnRequestAvailableLines(
                                    NearbyBusStopsFragment.this);
                            mBusStopLinesView.setVisibility(View.VISIBLE);

                            break;
                    }
                }
            } else {
                if (isVisible()) {
                    //noinspection ConstantConditions
                    FeedbackHelper.snackbar(getView(), observable.message, false);
                }
            }
        }

        @Override
        public void onCompleted() {
            mMaterialDialogHelper.dismissDialog();
        }

        @Override
        public void onError(Throwable e) {
            mMaterialDialogHelper.dismissDialog();
        }
    };

    @Override
    public void onLocationIsAvailable(@NonNull Location location) {
        Timber.i("Received location %f/%f", location.getLatitude(), location.getLongitude());

        if (NetworkUtil.isDeviceConnectedToInternet(getContext())) {
            mNearbyBusStopsApiSubscriber = new ApiSubscriber<>(
                    mNearbyBusStopsResponseRxDelegate);

            RetrofitController.instance(getContext())
                    .searchNearbyBusStops(location.getLatitude(), location.getLongitude(),
                            0.25f)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mNearbyBusStopsApiSubscriber);
        } else {
            FeedbackHelper.toast(getContext(), getString(R.string.feedback_no_network), false);
        }
    }

    @Override
    public void onConnectionError(int errorCode) {
        // Show dialog using GoogleApiAvailability.getErrorDialog()
        Timber.i("Received connection error with code %d", errorCode);
        showErrorDialog(errorCode);
    }

    @Override
    public void onRequestLocationPermission(PlayServicesHelper.PlayServicesPermission permission) {
        Timber.i("Received request to grant location permission %s", permission.getName());
        switch (permission) {
            case FINE_LOCATION:
                mMaterialDialogHelper.showDialog(getContext(), "Permissões",
                        "Precisamos acessar sua localização, por isto necessitamos que nos permita.",
                        "Aceitar",
                        (dialog, which) -> ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                REQUEST_LOCATION),
                        "Negar",
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        });
                break;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mPlayServicesHelper.setResolvingError(true);
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((NearbyBusStopsFragment)getParentFragment()).onDialogDismissed();
        }
    }
}
