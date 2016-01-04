package mx.x10.filipebezerra.horariosrmtcgoiania.main;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.squareup.otto.Subscribe;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.BusStopLinesResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber.ApiSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPredictionFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.asynctask.AsyncTaskCallback;
import mx.x10.filipebezerra.horariosrmtcgoiania.asynctask.AsyncTaskException;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseDrawerActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStopLinesFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.busterminal.BusTerminalFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.geocoder.LocationToAddressTask;
import mx.x10.filipebezerra.horariosrmtcgoiania.keyboard.KeyboardUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.nearby.NearbyBusStopsFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import mx.x10.filipebezerra.horariosrmtcgoiania.suggestion.BusStopSearchSuggestionsHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Main activity which contains the {@link android.support.v4.widget.DrawerLayout}.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class MainDrawerActivity extends BaseDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG = MainDrawerActivity.class.getSimpleName();

    private static final String ACTION_VOICE_SEARCH
            = "com.google.android.gms.actions.SEARCH_ACTION";

    private SearchView mSearchView;

    private MenuItem mSearchItem;

    private ApiSubscriber<BusStopLinesResponse> mBusStopLinesApiSubscriber;

    private LocationToAddressTask mLocationToAddressTask;

    @Bind(R.id.root_layout) protected CoordinatorLayout mRootLayout;

    @Override
    protected int provideLayoutResource() {
        return R.layout.activity_main_drawer;
    }

    @NonNull
    @Override
    public ViewGroup getRootViewLayout() {
        return mRootLayout;
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        Timber.tag(LOG);

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

            changeTitleAndSubtitle(getString(R.string.subtitle_nearby_bus_stops), null);
            replaceFragment(NearbyBusStopsFragment.newInstance(), false);
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_home) {
            changeTitleAndSubtitle(getString(R.string.subtitle_nearby_bus_stops), null);
            replaceFragment(NearbyBusStopsFragment.newInstance(), false);
        } else if (id == R.id.nav_bus_terminals) {
            changeTitleAndSubtitle(getString(R.string.title_terminals), null);
            replaceFragment(BusTerminalFragment.newInstance(), true);
        } else if (id == R.id.nav_favorites) {

        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.register(this);
    }

    @Override
    protected void onStop() {
        if (mBusStopLinesApiSubscriber != null) {
            if (!mBusStopLinesApiSubscriber.isUnsubscribed()) {
                mBusStopLinesApiSubscriber.unsubscribe();
            }

            mBusStopLinesApiSubscriber = null;
        }

        BusProvider.unregister(this);

        super.onStop();
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

                BusStopSearchSuggestionsHelper.saveSuggestion(MainDrawerActivity.this,
                        model.id,
                        model.address);

                if (mLocationToAddressTask != null) {
                    Timber.d("Cancelling LocationToAddressTask");
                    mLocationToAddressTask.cancel(true);
                }

                changeTitleAndSubtitle(String.format(getString(R.string.title_bus_stop_lines),
                        model.id), model.address);

                final BusStop busStop = BusStop.fromModel(model);

                BusStopLinesFragment fragment = BusStopLinesFragment.newInstance(busStop);

                Timber.d("Sending fragment %s to be added",
                        fragment.getClass().getSimpleName());
                replaceFragment(fragment, true);
            } else {
                FeedbackHelper.snackbar(getRootViewLayout(), observable.message, false,
                        new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                KeyboardUtil.focusThenShowKeyboard(MainDrawerActivity.this,
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

        if (mLocationToAddressTask != null) {
            Timber.d("Cancelling LocationToAddressTask");
            mLocationToAddressTask.cancel(true);
        }

        changeTitleAndSubtitle(String.format("%s - %s", arrivalPrediction.getLineNumber(),
                arrivalPrediction.getDestination()), getString(
                R.string.title_arrival_prediction));

        final ArrivalPredictionFragment fragment
                = ArrivalPredictionFragment.newInstance(arrivalPrediction);

        Timber.d("Sending fragment %s to be added", fragment.getClass().getSimpleName());

        replaceFragment(fragment, true);
    }

    @Subscribe
    public void onBusStopFound(BusStop busStop) {
        if (mLocationToAddressTask != null) {
            Timber.d("Cancelling LocationToAddressTask");
            mLocationToAddressTask.cancel(true);
        }

        changeTitleAndSubtitle(String.format(getString(R.string.title_bus_stop),
                busStop.getId()), busStop.getAddress());

        final BusStopLinesFragment fragment = BusStopLinesFragment.newInstance(busStop);

        Timber.d("Sending fragment %s to be added", fragment.getClass().getSimpleName());

        replaceFragment(fragment, true);
    }

    @Subscribe
    public void onLocationFound(Location location) {
        mLocationToAddressTask = new LocationToAddressTask(
                new AsyncTaskCallback<Address>() {
                    @Override
                    public void onBegin() {
                        Timber.d("Starting execution of LocationToAddressTask");
                    }

                    @Override
                    public void onSuccess(Address address) {
                        Timber.d("LocationToAddressTask result with address %s",
                                address.toString());

                        if (getSupportFragmentManager()
                                .findFragmentById(R.id.fragment_placeholder)
                                instanceof NearbyBusStopsFragment) {
                            Timber.d("Changing activity title and subtitle");
                            changeTitleAndSubtitle(address.getThoroughfare(),
                                    getString(R.string.subtitle_nearby_bus_stops));
                        } else {
                            Timber.d("NearbyBusStopsFragment not in container, title "
                                    + "and subtitle discarded");
                        }
                    }

                    @Override
                    public void onResultNothing() {
                        Timber.d("LocationToAddressTask result with nothing");
                    }

                    @Override
                    public void onError(AsyncTaskException e) {
                        Timber.e(e, "Error in LocationToAddressTask");
                    }
                }, this
        );
        mLocationToAddressTask.execute(location);
    }
}
