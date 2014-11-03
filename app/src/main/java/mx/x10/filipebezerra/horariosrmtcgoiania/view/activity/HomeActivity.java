package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.johnpersano.supertoasts.SuperCardToast;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.DrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.SacFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends BaseActivity {

    public static final int DEFAULT_MENU_ITEM = 0;
    private SearchView searchView;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mDrawerList;

    private DrawerAdapter mDrawerAdapter;

    private List<DrawerItem> mDrawerItems = new ArrayList<>();

    private String[] urls;
    private String[] drawerMenuTitles;
    private TypedArray drawerMenuIcons;

    private int mActiveMenuItem = DEFAULT_MENU_ITEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
        loadResoures();

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        SuperCardToast.onRestoreState(savedInstanceState, this);

        handleIntent(getIntent());

        setupDrawer();
        if (savedInstanceState == null) {
            displayView(DEFAULT_MENU_ITEM);
            mDrawerLayout.openDrawer(mDrawerList);
            mDrawerToggle.onDrawerOpened(mDrawerList);
        }
    }

    private void loadResoures() {
        drawerMenuTitles = getResources().getStringArray(R.array.drawer_menu_row_title);
        drawerMenuIcons = getResources().obtainTypedArray(R.array.drawer_menu_row_icon);
        urls = getResources().getStringArray(R.array.drawer_menu_row_url);
    }

    private void setupDrawer() {
        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        for (int i = 0; i < drawerMenuTitles.length; i++) {
            mDrawerItems.add(new DrawerItem(drawerMenuTitles[i],
                    drawerMenuIcons.getResourceId(i, -1)));
        }

        drawerMenuIcons.recycle();

        mDrawerAdapter = new DrawerAdapter(this, mDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_title_opened, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setSubtitle(R.string.drawer_title_opened);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setSubtitle(drawerMenuTitles[mActiveMenuItem]);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void displayView(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HorarioViagemFragment(urls[position]);
                break;
            case 1:
                fragment = new PlanejeViagemFragment(urls[position]);
                break;
            case 2:
                fragment = new PontoToPontoFragment(urls[position]);
                break;
            case 3:
                fragment = new SacFragment(urls[position]);
                break;
            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
            mActiveMenuItem = position;
        } else {
            // TODO samethins is wrong
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            displayView(position);
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
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (TextUtils.isDigitsOnly(query)) {
                searchView.setQuery(query, false);
                searchView.requestFocus();

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
            } else {
                new ToastHelper(this).showGeneralMessage(getResources()
                        .getString(R.string.non_digit_voice_search));
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

}