package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.otto.Subscribe;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.BusStopLinesResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.NearbyBusStopsResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber.ApiSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPredictionFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseDrawerActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStopLinesFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.keyboard.KeyboardUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.nearby.NearbyBusStopsFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.PlayServicesPermission;
import mx.x10.filipebezerra.horariosrmtcgoiania.suggestion.BusStopSearchSuggestionsHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_RESOLVE_ERROR;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.PlayServicesPermission.FINE_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.STATE_LAST_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.playservices.PlayServicesHelper.STATE_RESOLVING_ERROR;

/**
 * Home and main screen
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class HomeActivity extends BaseDrawerActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        PlayServicesHelper.PlayServicesCallbacks,
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final String ACTION_VOICE_SEARCH
            = "com.google.android.gms.actions.SEARCH_ACTION";
    private static final String LOG = HomeActivity.class.getSimpleName();

    private SearchView mSearchView;

    private MenuItem mSearchItem;
    private ApiSubscriber<BusStopLinesResponse> mBusStopLinesApiSubscriber;

    private ApiSubscriber<NearbyBusStopsResponse> mNearbyBusStopsApiSubscriber;

    private PlayServicesHelper mPlayServicesHelper;

    private static final String DIALOG_ERROR = "dialog_error";

    @Bind(R.id.root_layout) protected CoordinatorLayout mRootLayout;

    @Override
    protected int provideLayoutResource() {
        return R.layout.activity_home;
    }

    @NonNull
    @Override
    public ViewGroup getRootViewLayout() {
        return mRootLayout;
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        Timber.tag(TAG);

        mPlayServicesHelper = new PlayServicesHelper(this, inState, this);

        if (inState == null) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favorite);
            DrawableHelper.tint(this, R.color.white, fab.getDrawable());
            fab.setOnClickListener(view ->
                    FeedbackHelper.snackbar(view, "Replace with your own action", true,
                            "Action", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }));

            addOnBackStackChangedListener(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.i("New intent received.");
        handleSearchQuery(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        DrawableHelper.tint(this, R.color.white, mSearchItem.getIcon());
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        handleSearchQuery(getIntent());

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.register(this);
        mPlayServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        if (mBusStopLinesApiSubscriber != null) {
            if (!mBusStopLinesApiSubscriber.isUnsubscribed()) {
                mBusStopLinesApiSubscriber.unsubscribe();
            }

            mBusStopLinesApiSubscriber = null;
        }

        if (mNearbyBusStopsApiSubscriber != null) {
            if (!mNearbyBusStopsApiSubscriber.isUnsubscribed()) {
                mNearbyBusStopsApiSubscriber.unsubscribe();
            }

            mNearbyBusStopsApiSubscriber = null;
        }

        mPlayServicesHelper.disconnect();

        BusProvider.unregister(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mPlayServicesHelper.isResolvingError());
        outState.putParcelable(STATE_LAST_LOCATION, mPlayServicesHelper.getLastLocation());
    }

    private SubscriberDelegate<BusStopLinesResponse> mBusStopLinesRxDelegate
            = new SubscriberDelegate<BusStopLinesResponse>() {
        @Override
        public void onStart() {
            mMaterialDialogHelper
                    .showIndeterminateProgress(getString(R.string.feedback_searching_bus_stop),
                            true,
                            dialog -> {
                                mMaterialDialogHelper.dismissDialog();
                                mBusStopLinesApiSubscriber.unsubscribe();
                            });
        }

        @Override
        public void onNext(BusStopLinesResponse observable) {
            mSearchView.clearFocus();

            if (Boolean.parseBoolean(observable.status)) {
                MenuItemCompat.collapseActionView(mSearchItem);

                final BusStopModel model = observable.data;

                BusStopSearchSuggestionsHelper.saveSuggestion(HomeActivity.this,
                        model.id,
                        model.address);

                changeTitleAndSubtitle(String.format("Ponto %s", model.id), model.address);

                final BusStop busStop = BusStop.fromModel(model);

                BusStopLinesFragment fragment = BusStopLinesFragment.newInstance(busStop);

                Timber.d("Sending fragment %s to be added",
                        fragment.getClass().getSimpleName());
                replaceFragment(fragment, true);

                // TODO remove commented block
                /*if (exists) {
                    Timber.d("The fragment %s already exists in the container. Sending "
                                    + "data via event", fragment.getClass().getSimpleName());

                    BusProvider.post(new GenericEvent<>(busStop));
                }*/
            } else {
                FeedbackHelper.snackbar(getRootViewLayout(), observable.message, false,
                        new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                KeyboardUtil.focusThenShowKeyboard(HomeActivity.this,
                                        mSearchView);
                            }
                        });
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

    private SubscriberDelegate<NearbyBusStopsResponse> mNearbyBusStopsResponseRxDelegate
            = new SubscriberDelegate<NearbyBusStopsResponse>() {
        @Override
        public void onStart() {
            mMaterialDialogHelper
                    .showIndeterminateProgress(getString(R.string.feedback_searching_nearby_bus_stop),
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

                            changeTitleAndSubtitle(String.format("Ponto próximo %s",
                                    busStop.getId()), busStop.getAddress());

                            BusStopLinesFragment busStopFragment = BusStopLinesFragment
                                    .newInstance(busStop);

                            Timber.d("Sending fragment %s to be added",
                                    busStopFragment.getClass().getSimpleName());
                            replaceFragment(busStopFragment, true);

                            // TODO remove commented block
                            /*if (exists) {
                                Timber.d("The fragment %s already exists in the container. "
                                                + "Sending data via event",
                                        busStopFragment.getClass().getSimpleName());

                                BusProvider.post(new GenericEvent<>(busStop));
                            }*/

                            break;

                        default:
                            final List<BusStop> busStops = BusStop.fromModel(model);

                            changeTitleAndSubtitle("Pontos próximos à você", "");

                            NearbyBusStopsFragment nearbyFragment = NearbyBusStopsFragment
                                    .newInstance(busStops);

                            Timber.d("Sending fragment %s to be added",
                                    nearbyFragment.getClass().getSimpleName());
                            replaceFragment(nearbyFragment, true);

                            // TODO remove commented block
                            /*if (exists) {
                                Timber.d("The fragment %s already exists in the container. "
                                                + "Sending data via event",
                                        nearbyFragment.getClass().getSimpleName());

                                BusProvider.post(new GenericEvent<>(busStops));
                            }*/

                            break;
                    }
                }
            } else {
                FeedbackHelper.snackbar(getRootViewLayout(), observable.message, false);
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

    private void handleSearchQuery(Intent intent) {
        if (intent != null) {
            Timber.i("Handling the new intent.");
            if (Intent.ACTION_SEARCH.equals(intent.getAction()) ||
                    ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                if (ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                    Timber.i("Action voice search intent, setting up the SearchView.");
                    mSearchView.setQuery(query, true);
                } else {
                    if (!TextUtils.isEmpty(query)) {
                        Timber.i("Intent comes from the SearchView, now performing the search strategy.");
                        performSearch(query);
                    } else {
                        Timber.i("Intent comes from the SearchView but was empty.");
                    }
                }
            }
        }
    }

    private void performSearch(String query) {
        if (NetworkUtil.isDeviceConnectedToInternet(this)) {
            mBusStopLinesApiSubscriber = new ApiSubscriber<>(mBusStopLinesRxDelegate);

            RetrofitController.instance(this)
                    .searchBusStopLines(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mBusStopLinesApiSubscriber);
        } else {
            FeedbackHelper.toast(this, getString(R.string.feedback_no_network), false);
        }
    }
    @Subscribe
    public void onArrivalPredictionFound(GenericEvent<ArrivalPrediction> event) {
        Timber.d("%s observer received the event with data %s", LOG,
                event.message().toString());

        final ArrivalPrediction arrivalPrediction = event.message();
        changeTitleAndSubtitle("Próxima viagem", String.format("%s - %s",
                arrivalPrediction.getLineNumber(), arrivalPrediction.getDestination()));

        final ArrivalPredictionFragment fragment
                = ArrivalPredictionFragment.newInstance(arrivalPrediction);

        Timber.d("Sending fragment %s to be added", fragment.getClass().getSimpleName());

        replaceFragment(fragment, true);

        // TODO remove commented block
        /*if (exists) {
            Timber.d("The fragment %s already exists in the container. Sending data via event",
                    fragment.getClass().getSimpleName());
        }*/
    }

    @SuppressWarnings("ResourceType")
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            boolean isErrorSolved = resultCode == RESULT_OK;
            Timber.i("Connection error was solved = %b", isErrorSolved);
            mPlayServicesHelper.setResolvingError(isErrorSolved);
        }
    }

    @Override
    public void onLocationIsAvailable(@NonNull Location location) {
        Timber.i("Received location %f/%f", location.getLatitude(), location.getLongitude());

        if (!mIsFragmentRestored) {
            if (NetworkUtil.isDeviceConnectedToInternet(this)) {
                mNearbyBusStopsApiSubscriber = new ApiSubscriber<>(
                        mNearbyBusStopsResponseRxDelegate);

                RetrofitController.instance(this)
                        .searchNearbyBusStops(location.getLatitude(), location.getLongitude(),
                                0.25f)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mNearbyBusStopsApiSubscriber);
            } else {
                FeedbackHelper.toast(this, getString(R.string.feedback_no_network), false);
            }
        } else {
            Timber.d("Last fragment was restored");
        }
    }

    @Override
    public void onConnectionError(int errorCode) {
        // Show dialog using GoogleApiAvailability.getErrorDialog()
        Timber.i("Received connection error with code %d", errorCode);
        showErrorDialog(errorCode);
    }

    @Override
    public void onRequestLocationPermission(PlayServicesPermission permission) {
        Timber.i("Received request to grant location permission %s", permission.getName());
        switch (permission) {
            case FINE_LOCATION:
                mMaterialDialogHelper.showDialog(this, "Permissões",
                        "Precisamos acessar sua localização, por isto necessitamos que nos permita.",
                        "Aceitar",
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ActivityCompat.requestPermissions(
                                        HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        },
                        "Negar",
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        });
                break;
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Timber.d("No fragments found in the back stack. Finishing application");
            finish();
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
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
            ((HomeActivity) getActivity()).onDialogDismissed();
        }
    }
}
