package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.favoriteslist.FavoritesListFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.home.HomeFragment;

/**
 * @author Filipe Bezerra
 */
class TabsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = Collections.unmodifiableList(Arrays.asList(
                HomeFragment.newInstance(),
                FavoritesListFragment.newInstance()
        ));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
