package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 27/02/2015
 * @since #
 */
public class WapFragment extends BaseWebViewFragment {
    
    public WapFragment() {
    }
    
    @Override
    protected String getUrlToLoad() {
        return getString(R.string.url_rmtc_wap);
    }
    
}
