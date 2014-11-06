package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperCardToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    private static final String ARG_PARAM_BUS_STOP_NUMBER = "BUS_STOP_NUMBER";

    private String busStopNumber = null;

    public HorarioViagemFragment() {
    }

    public static HorarioViagemFragment newInstance(final String busStopNumber) {
        HorarioViagemFragment fragment = new HorarioViagemFragment();

        if (busStopNumber != null) {
            Bundle args = new Bundle();
            args.putString(ARG_PARAM_BUS_STOP_NUMBER, busStopNumber);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_PARAM_BUS_STOP_NUMBER)) {
                busStopNumber = args.getString(ARG_PARAM_BUS_STOP_NUMBER);
            }
        }
    }

    @Override
    protected String getUrlToLoad() {
        return busStopNumber == null ? getString(R.string.url_rmtc_horarios_viagem) :
                String.format(getString(R.string.formatted_url_rmtc_horarios_viagem),
                        getString(R.string.url_rmtc_horarios_viagem),
                        getString(R.string.resource_visualizar_ponto), busStopNumber);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SuperCardToast.onRestoreState(savedInstanceState, getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
