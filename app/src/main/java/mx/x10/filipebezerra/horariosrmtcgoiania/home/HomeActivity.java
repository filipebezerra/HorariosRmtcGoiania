package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

public class HomeActivity extends AppCompatActivity {

    private static final int TAB_HOME = 0;
    private static final int TAB_FAVORITES = 1;

    private static final String ACTION_VOICE_SEARCH
            = "com.google.android.gms.actions.SEARCH_ACTION";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private MenuItem mSearchItem;

    private SearchView mSearchView;

    @Bind(R.id.main_content) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fragment_container) ViewPager mViewPager;

    class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_HOME: return HomeFragment.newInstance();
                case TAB_FAVORITES: return FavoritesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void handleSearchIntent(Intent intent) {
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction()) ||
                    ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                if (ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                    mSearchView.setQuery(query, true);
                } else {
                    if (!TextUtils.isEmpty(query)) {
                        performSearch(query);
                    }
                }
            }
        }
    }

    private void performSearch(String query) {
        Snackbar.make(mCoordinatorLayout, "Buscando ponto " + query, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = ButterKnife.findById(this, R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        switch (tab.getPosition()) {
                            case TAB_HOME:
                                tab.setIcon(R.drawable.ic_home_selected);
                                break;
                            case TAB_FAVORITES:
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
                            case TAB_FAVORITES:
                                tab.setIcon(R.drawable.ic_favorite_unselected);
                                break;
                        }
                    }
                });

        tabLayout.getTabAt(TAB_HOME).setIcon(R.drawable.ic_home_selected);
        tabLayout.getTabAt(TAB_FAVORITES).setIcon(R.drawable.ic_favorite_unselected);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleSearchIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        mSearchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        handleSearchIntent(getIntent());

        return true;
    }
}
