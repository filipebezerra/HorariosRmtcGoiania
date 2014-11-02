package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.DrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private SearchView searchView;

    private DrawerLayout mDrawer;

    private ListView mListDrawer;

    private DrawerAdapter mDrawerAdapter;

    private List<DrawerItem> mDrawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        SuperCardToast.onRestoreState(savedInstanceState, this);

        handleIntent(getIntent());

        setupDrawer();
    }

    private void setupDrawer() {
        String[] drawerMenuTitles = getResources().getStringArray(R.array.drawer_menu_row_title);
        TypedArray drawerMenuIcons = getResources().obtainTypedArray(R.array.drawer_menu_row_icon);

        mListDrawer = (ListView) findViewById(R.id.listView);

        for (int i = 0; i < drawerMenuTitles.length; i++) {
            mDrawerItems.add(new DrawerItem(drawerMenuTitles[i],
                    drawerMenuIcons.getResourceId(i, -1)));
        }

        drawerMenuIcons.recycle();

        mDrawerAdapter = new DrawerAdapter(this, mDrawerItems);
        mListDrawer.setAdapter(mDrawerAdapter);
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
                searchView.clearFocus();

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
            } else {
                SuperCardToast toast = new SuperCardToast(this, SuperToast.Type.STANDARD);
                toast.setAnimations(SuperToast.Animations.FLYIN);
                toast.setDuration(SuperToast.Duration.LONG);
                toast.setBackground(SuperToast.Background.BLUE);
                toast.setTextSize(SuperToast.TextSize.MEDIUM);
                toast.setSwipeToDismiss(true);
                toast.setTouchToDismiss(true);
                toast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                toast.setText(getResources().getString(R.string.non_digit_voice_search));
                toast.show();
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
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    @Override
    public void onDrawerSlide(View view, float v) {

    }

    @Override
    public void onDrawerOpened(View view) {

    }

    @Override
    public void onDrawerClosed(View view) {

    }

    @Override
    public void onDrawerStateChanged(int i) {

    }
}