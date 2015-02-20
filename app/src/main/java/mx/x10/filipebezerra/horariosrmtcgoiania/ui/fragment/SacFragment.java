package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.os.Bundle;
import android.view.View;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class SacFragment extends BaseWebViewFragment {

    public SacFragment(){}

    @Override
    protected String getUrlToLoad() {
        return getString(R.string.url_rmtc_sac);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
