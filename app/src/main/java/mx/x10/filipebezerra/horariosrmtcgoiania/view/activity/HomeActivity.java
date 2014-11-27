package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

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

import com.github.johnpersano.supertoasts.SuperCardToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.HorariosRmtcGoianiaApplication;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuSection;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.util.fragment.NavDrawerActivityConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends AbstractNavDrawerActivity {

    private static final int ID_NAV_MENU_SECTION_SEUS_ITENS = 10;
    private static final int ID_NAV_MENU_SECTION_SERVICOS_RMTC = 20;
    private static final int ID_NAV_MENU_SECTION_OUTROS = 30;

    private static final int ID_NAV_MENU_ITEM_PONTOS_FAVORITOS = 11;
    private static final int ID_NAV_MENU_ITEM_HORARIOS_VIAGEM = 21;
    private static final int ID_NAV_MENU_ITEM_PLANEJE_VIAGEM = 22;
    private static final int ID_NAV_MENU_ITEM_PONTO_A_PONTO = 23;
    private static final int ID_NAV_MENU_ITEM_SAC = 24;

    public static final int DEFAULT_NAV_MENU_ITEM_SELECTED = 3;

    public static final int SEARCH_RESULT_VIEW = ID_NAV_MENU_ITEM_HORARIOS_VIAGEM;

    private SearchView mSearchView;

    private String query = null;

    private Fragment currentView;

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        final NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(ID_NAV_MENU_SECTION_SEUS_ITENS, getString(R.string.drawer_section_user_items)),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PONTOS_FAVORITOS, getString(R.string.navdrawer_item_favorites_bus_stops), "ic_favorite_white_24dp", "10", true, false, this),

                NavMenuSection.create(ID_NAV_MENU_SECTION_SERVICOS_RMTC, getString(R.string.drawer_section_rmtc_services)),
                NavMenuItem.create(ID_NAV_MENU_ITEM_HORARIOS_VIAGEM, getString(R.string.navdrawer_item_rmtc_horarios_viagem), "ic_alarm_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PLANEJE_VIAGEM, getString(R.string.navdrawer_item_rmtc_planeje_viagem), "ic_map_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_PONTO_A_PONTO, getString(R.string.navdrawer_item_rmtc_ponto_a_ponto), "ic_place_white_24dp", true, this),
                NavMenuItem.create(ID_NAV_MENU_ITEM_SAC, getString(R.string.navdrawer_item_rmtc_sac), "ic_call_white_24dp", true, this),

                NavMenuSection.create(ID_NAV_MENU_SECTION_OUTROS, getString(R.string.drawer_section_others))
        };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);

        // TODO: Set the Drawer Shadow
        navDrawerActivityConfiguration.setDrawerShadow(0);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_title_opened);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_title_closed);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu));

        // TODO: Maybe in the future, disabling the menu items
        navDrawerActivityConfiguration.setActionMenuItemsToHideWhenDrawerOpen(null);

        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        currentView = null;

        switch (id) {
            case ID_NAV_MENU_ITEM_PONTOS_FAVORITOS:
                openRightDrawer();
                break;
            case ID_NAV_MENU_ITEM_HORARIOS_VIAGEM:
                currentView = HorarioViagemFragment.newInstance(query == null ? null : query);
                break;
            case ID_NAV_MENU_ITEM_PLANEJE_VIAGEM:
                currentView = new PlanejeViagemFragment();
                break;
            case ID_NAV_MENU_ITEM_PONTO_A_PONTO:
                currentView = new PontoToPontoFragment();
                break;
            case ID_NAV_MENU_ITEM_SAC:
                currentView = new SacFragment();
                break;
            default:
                break;
        }

        if (currentView != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    currentView).commit();
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
        if (Intent.ACTION_SEARCH == intent.getAction()) {
            doSearch(intent);
        }
    }

    private void doSearch(Intent intent) {
        query = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(query)) {
            mSearchView.clearFocus();
            mSearchView.setQuery(query, false);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            onNavItemSelected(SEARCH_RESULT_VIEW);
        } else {
            new ToastHelper(this).showGeneralAlert(getResources()
                    .getString(R.string.non_digit_voice_search));
        }

        query = null;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_mark_as_favorite:
                if (currentView instanceof HorarioViagemFragment) {
                    String currentUrl = ((HorarioViagemFragment) currentView).getWebView().getUrl();

                    int idx = currentUrl.lastIndexOf("/");
                    String stopCode = currentUrl.substring(idx);

                    ((HorariosRmtcGoianiaApplication) getApplicationContext())
                            .getDaoSession()
                            .getFavoriteBusStopDao()
                            .queryBuilder()
                            .where(FavoriteBusStopDao.Properties.StopCode.eq(1))
                            .unique();
                }
                break;
        }

        return false;
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