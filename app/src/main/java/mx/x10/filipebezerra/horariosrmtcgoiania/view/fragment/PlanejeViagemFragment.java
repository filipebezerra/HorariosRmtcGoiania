package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.os.Bundle;
import android.view.View;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class PlanejeViagemFragment extends BaseWebViewFragment {

    public PlanejeViagemFragment(){

    }

    @Override
    protected String getUrlToLoad() {
        return getString(R.string.url_rmtc_planeje_viagem);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
