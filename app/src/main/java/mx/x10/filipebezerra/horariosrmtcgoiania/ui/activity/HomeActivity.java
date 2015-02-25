package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.squareup.otto.Subscribe;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.BasicNavigationDrawerEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NavigationDrawerSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.AbstractNavDrawerActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.NavDrawerActivityConfiguration;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_PONTO_A_PONTO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_MENU_ITEM_SAC;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends AbstractNavDrawerActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    public static final int SEARCH_RESULT_VIEW = ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM;

    private SearchView mSearchView;

    private String mLastQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : read Android doc for this method
        //setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        SuperCardToast.onRestoreState(savedInstanceState, this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        NavDrawerActivityConfiguration navDrawerConf = new NavDrawerActivityConfiguration();
        navDrawerConf.setDrawerLayoutId(R.id.drawer_container);
        navDrawerConf.setDrawerShadow(0);
        navDrawerConf.setDrawerOpenDesc(R.string.drawer_title_opened);
        navDrawerConf.setDrawerCloseDesc(R.string.drawer_title_closed);

        return navDrawerConf;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Subscribe
    public void onNavigationDrawerSelectionEvent(NavigationDrawerSelectionEvent event) {
        final int position = event.getPosition();
        final int gravity = event.getGravity();
        Fragment fragment = null;

        if (gravity == Gravity.LEFT) {
            switch (event.getPosition()) {
                case ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS:
                    openDrawer(Gravity.RIGHT);
                    break;
                case ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM:
                    fragment = HorarioViagemFragment.newInstance(
                            mLastQuery == null ? null : mLastQuery);
                    break;
                case ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM:
                    fragment = new PlanejeViagemFragment();
                    break;
                case ID_DRAWER_MENU_ITEM_PONTO_A_PONTO:
                    fragment = new PontoToPontoFragment();
                    break;
                case ID_DRAWER_MENU_MENU_ITEM_SAC:
                    fragment = new SacFragment();
                    break;
                default:
                    break;
            }
        } else if (gravity == Gravity.RIGHT) {
            fragment = HorarioViagemFragment.newInstance(String.valueOf(position));
        } else {
            Log.d(LOG_TAG, String.format("No navigation drawer set for gravity %d", gravity));
        }

        if (fragment != null) {
            super.onNavigationDrawerSelectionEvent(event);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        } else {
            Log.d(LOG_TAG, String.format("No fragment found for NavItem %d selected!", position));
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            doSearch(intent);
        }
    }

    private void doSearch(Intent intent) {
        mLastQuery = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(mLastQuery)) {
            mSearchView.clearFocus();
            mSearchView.setQuery(mLastQuery, false);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
            suggestions.saveRecentQuery(mLastQuery, null);
            mEventBus.post(new BasicNavigationDrawerEvent(SEARCH_RESULT_VIEW));
        } else {
            new ToastHelper(this).showInformation(getResources()
                    .getString(R.string.non_digit_voice_search));
        }

        mLastQuery = null;
    }

}