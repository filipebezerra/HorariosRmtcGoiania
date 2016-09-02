package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.main;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.activity.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.ActivityNavigator;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.IntentHandler;

/**
 * @author Filipe Bezerra
 */
public class MainActivity extends BaseActivity implements MainContract.View {
    private static final int TAB_HOME = 0;
    private static final int TAB_FAVORITES_LIST = 1;

    @BindView(R.id.main_container) protected CoordinatorLayout mMainContainerLayout;
    @BindView(R.id.view_pager) protected ViewPager mViewPager;
    @BindView(R.id.tabs) protected TabLayout mTabLayout;

    private MenuItem mSearchItem;

    private SearchView mSearchView;

    private MainContract.Presenter mPresenter;

    private IntentHandler mSearchIntentHandler;

    @Override
    protected int provideViewResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter(this);
        mSearchIntentHandler = new SearchIntentHandler(mPresenter);
        setupViewPager();
        setupTabs();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mSearchIntentHandler.handleSearchIntent(intent);
    }

    private void setupViewPager() {
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabs() {
        mTabLayout.getTabAt(TAB_HOME).setIcon(R.drawable.ic_home_selected);
        mTabLayout.getTabAt(TAB_FAVORITES_LIST).setIcon(R.drawable.ic_favorite_unselected);
        mTabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        switch (tab.getPosition()) {
                            case TAB_HOME:
                                tab.setIcon(R.drawable.ic_home_selected);
                                break;
                            case TAB_FAVORITES_LIST:
                                tab.setIcon(R.drawable.ic_favorite_selected);
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        switch (tab.getPosition()) {
                            case TAB_HOME:
                                tab.setIcon(R.drawable.ic_home_unselected);
                                break;
                            case TAB_FAVORITES_LIST:
                                tab.setIcon(R.drawable.ic_favorite_unselected);
                                break;
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchIntentHandler.handleSearchIntent(getIntent());
        return true;
    }

    @Override
    public void navigateToLinhasOnibusScreen(String numeroPonto) {
        ActivityNavigator.navigateToLinhasOnibusActivity(this, numeroPonto);
    }

    @Override
    public void showSearchResult(String result) {
        mSearchView.setQuery(result, true);
    }
}
