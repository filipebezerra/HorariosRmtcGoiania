package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.home;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.fragment.BaseFragment;

/**
 * @author Filipe Bezerra
 */
public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int provideViewResource() {
        return R.layout.fragment_home;
    }
}
