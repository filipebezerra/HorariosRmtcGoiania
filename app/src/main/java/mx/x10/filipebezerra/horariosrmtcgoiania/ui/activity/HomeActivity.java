package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.github.johnpersano.supertoasts.SuperCardToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.FavoriteBusStopsAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuSection;
import mx.x10.filipebezerra.horariosrmtcgoiania.parser.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.util.fragment.NavDrawerActivityConfiguration;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends AbstractNavDrawerActivity {

    private static final int ID_NAV_MENU_SECTION_USER_CUSTOM_ITEMS = 10;
    private static final int ID_NAV_MENU_ITEM_FAVORITE_BUS_STOPS = 11;

    private static final int ID_NAV_MENU_SECTION_SERVICOS_RMTC = 20;
    private static final int ID_NAV_MENU_ITEM_HORARIOS_VIAGEM = 21;
    private static final int ID_NAV_MENU_ITEM_PLANEJE_VIAGEM = 22;
    private static final int ID_NAV_MENU_ITEM_PONTO_A_PONTO = 23;
    private static final int ID_NAV_MENU_ITEM_SAC = 24;

    private static final int ID_NAV_MENU_SECTION_ABOUT = 30;
    private static final int ID_NAV_MENU_ITEM_PRODUCT_TOUR = 31;
    private static final int ID_NAV_MENU_ITEM_PRODUCT_SHARE = 32;
    private static final int ID_NAV_MENU_ITEM_PRODUCT_EVALUATE = 33;
    private static final int ID_NAV_MENU_ITEM_USER_PREFERENCES = 34;

    public static final int DEFAULT_NAV_MENU_ITEM_SELECTED = 3;
    public static final int SEARCH_RESULT_VIEW = ID_NAV_MENU_ITEM_HORARIOS_VIAGEM;

    private SearchView mSearchView;
    private String mLastQuery = null;
    private Fragment mCurrentFragmentView;

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        final NavDrawerItem[] menu = new NavDrawerItem[]{
                NavMenuSection.create(ID_NAV_MENU_SECTION_USER_CUSTOM_ITEMS, getString(R.string.drawer_section_user_custom_items)),
                NavMenuItem.create(ID_NAV_MENU_ITEM_FAVORITE_BUS_STOPS, getString(R.string.navdrawer_item_favorite_bus_stops), "ic_favorite_white_24dp", String.valueOf(ApplicationSingleton.getInstance().getDaoSession().getFavoriteBusStopDao().count()), true, false, this),

                NavMenuSection.create(ID_NAV_MENU_SECTION_SERVICOS_RMTC, getString(R.string.drawer_section_rmtc_services)),
                NavMenuItem.create(ID_NAV_MENU_ITEM_HORARIOS_VIAGEM, getString(R.string.navdrawer_item_rmtc_horarios_viagem), "ic_alarm_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PLANEJE_VIAGEM, getString(R.string.navdrawer_item_rmtc_planeje_viagem), "ic_map_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PONTO_A_PONTO, getString(R.string.navdrawer_item_rmtc_ponto_a_ponto), "ic_place_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_SAC, getString(R.string.navdrawer_item_rmtc_sac), "ic_call_white_24dp", true, this),

                NavMenuSection.create(ID_NAV_MENU_SECTION_ABOUT, getString(R.string.drawer_section_about)),

                NavMenuItem.create(ID_NAV_MENU_ITEM_PRODUCT_TOUR, getString(R.string.navdrawer_item_product_tour), "ic_flight_white_24dp", false, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PRODUCT_SHARE, getString(R.string.navdrawer_item_product_share), "ic_share_white_24dp", false, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PRODUCT_EVALUATE, getString(R.string.navdrawer_item_product_evaluate), "ic_stars_white_24dp", false, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_USER_PREFERENCES, getString(R.string.navdrawer_item_user_preferences), "ic_settings_white_24dp", false, this)
        };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setRightDrawerId(R.id.right_drawer);
        navDrawerActivityConfiguration.setLeftNavItems(menu);

        // TODO: Set the Drawer Shadow
        navDrawerActivityConfiguration.setDrawerShadow(0);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_title_opened);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_title_closed);
        navDrawerActivityConfiguration.setLeftNavAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_left_items, menu));
        navDrawerActivityConfiguration.setRightNavAdapter(
                new FavoriteBusStopsAdapter(this, ApplicationSingleton.getInstance()
                        .getDaoSession().getFavoriteBusStopDao().loadAll()));

        // TODO: Maybe in the future, disabling the menu items
        navDrawerActivityConfiguration.setActionMenuItemsToHideWhenDrawerOpen(null);

        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        mCurrentFragmentView = null;

        switch (id) {
            case ID_NAV_MENU_ITEM_FAVORITE_BUS_STOPS:
                openRightDrawer();
                break;
            case ID_NAV_MENU_ITEM_HORARIOS_VIAGEM:
                mCurrentFragmentView = HorarioViagemFragment.newInstance(mLastQuery == null ? null : mLastQuery);
                break;
            case ID_NAV_MENU_ITEM_PLANEJE_VIAGEM:
                mCurrentFragmentView = new PlanejeViagemFragment();
                break;
            case ID_NAV_MENU_ITEM_PONTO_A_PONTO:
                mCurrentFragmentView = new PontoToPontoFragment();
                break;
            case ID_NAV_MENU_ITEM_SAC:
                mCurrentFragmentView = new SacFragment();
                break;
            default:
                break;
        }

        if (mCurrentFragmentView != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mCurrentFragmentView).commit();
        } else {
            // TODO: catch some error here
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        SuperCardToast.onRestoreState(savedInstanceState, this);

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            selectItem(DEFAULT_NAV_MENU_ITEM_SELECTED);
            openLeftDrawer();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction() == Intent.ACTION_SEARCH) {
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

            onNavItemSelected(SEARCH_RESULT_VIEW);
        } else {
            new ToastHelper(this).showInformation(getResources()
                    .getString(R.string.non_digit_voice_search));
        }

        mLastQuery = null;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
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

}