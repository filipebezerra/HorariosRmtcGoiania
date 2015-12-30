package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseDrawerActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStopLinesFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.dialog.MaterialDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.GenericEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.feedback.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.keyboard.KeyboardUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.NetworkUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RetrofitController;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import mx.x10.filipebezerra.horariosrmtcgoiania.suggestion.BusStopSearchSuggestionsHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Home and main screen
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class HomeActivity extends BaseDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG = HomeActivity.class.getSimpleName();

    private static final String ACTION_VOICE_SEARCH
            = "com.google.android.gms.actions.SEARCH_ACTION";

    @Bind(R.id.root_layout)
    protected CoordinatorLayout mRootLayout;

    private SearchView mSearchView;
    private MenuItem mSearchItem;

    private MaterialDialogHelper mMaterialDialogHelper;

    private ApiSubscriber<BusStopLinesResponse> mBusStopLinesSubscriber;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.tag(LOG);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favorite);
        DrawableHelper.tint(this, R.color.white, fab.getDrawable());
        fab.setOnClickListener(view ->
                FeedbackHelper.snackbar(view, "Replace with your own action", true,
                        "Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));

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
    protected void onResume() {
        super.onResume();
        mMaterialDialogHelper = MaterialDialogHelper.toContext(this);
        BusProvider.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBusStopLinesSubscriber != null) {
            if (mBusStopLinesSubscriber.isUnsubscribed()) {
                mBusStopLinesSubscriber.unsubscribe();
            }

            mBusStopLinesSubscriber = null;
        }

        BusProvider.unregister(this);
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
                                mBusStopLinesSubscriber.unsubscribe();
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

                final Fragment existingFragment = getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_placeholder);

                if (existingFragment == null) {
                    BusStopLinesFragment fragment = BusStopLinesFragment
                            .newInstance(busStop);
                    replaceFragment(fragment, true);
                } else {
                    BusProvider.postOnMain(new GenericEvent<>(busStop));
                }
            } else {
                FeedbackHelper.snackbar(getRootViewLayout(), observable.message, false,
                        new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                KeyboardUtil.focusThenShowKeyboard(HomeActivity.this, mSearchView);
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
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
            mBusStopLinesSubscriber = new ApiSubscriber<>(mBusStopLinesRxDelegate);

            RetrofitController.instance(this)
                    .searchBusStopLines(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mBusStopLinesSubscriber);
        } else {
            FeedbackHelper.toast(this, getString(R.string.feedback_no_network), false);
        }
    }

    @Subscribe
    public void onArrivalPredictionFound(GenericEvent<ArrivalPrediction> event) {
        replaceFragment(ArrivalPredictionFragment.newInstance(event.message()), true);
    }
}
