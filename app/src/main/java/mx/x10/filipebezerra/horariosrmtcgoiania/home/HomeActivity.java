package mx.x10.filipebezerra.horariosrmtcgoiania.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

public class HomeActivity extends AppCompatActivity {

    private static final int TAB_HOME = 0;
    private static final int TAB_FAVORITES = 1;

    private SectionsPagerAdapter mSectionsPagerAdapter;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
