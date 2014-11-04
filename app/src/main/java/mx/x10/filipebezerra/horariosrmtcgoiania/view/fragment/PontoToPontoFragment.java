package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.os.Bundle;
import android.view.View;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class PontoToPontoFragment extends BaseWebViewFragment {

    public PontoToPontoFragment(){

    }

    @Override
    protected String getUrlToLoad() {
        return getString(R.string.url_rmtc_ponto_a_ponto);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
