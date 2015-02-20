package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.github.johnpersano.supertoasts.SuperCardToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.parser.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;

/**
 * @author Filipe Bezerra
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    private static final String ARG_PARAM_BUS_STOP_NUMBER = "BUS_STOP_NUMBER";

    private static final String TAG_REQUEST = "tagStringRequest_"
            + String.valueOf(System.currentTimeMillis());

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
    protected String getUrlToLoad() {
        String busStopNumber = null;
        Bundle args = getArguments();

        if (args != null) {
            if (args.containsKey(ARG_PARAM_BUS_STOP_NUMBER)) {
                busStopNumber = args.getString(ARG_PARAM_BUS_STOP_NUMBER);
            }
        }

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.horario_viagem_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_mark_as_favorite:
                String currentUrl = getWebView().getUrl();

                if (!currentUrl.contains("/visualizar/ponto")) {
                    new ToastHelper(getActivity()).showWarning("Você deve buscar por um ponto de parada.");
                    return false;
                }

                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Buscando parada...");
                dialog.setIndeterminate(true);
                dialog.show();

                StringUTF8Request request = new StringUTF8Request(currentUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                FavoriteBusStop favoriteBusStop = new BusStopHtmlParser()
                                        .parse(result);
                                //mAdapter.add(favoriteBusStop);

                                FavoriteBusStopDao dao = ApplicationSingleton.getInstance().getDaoSession()
                                        .getFavoriteBusStopDao();

                                FavoriteBusStop favoriteBusStopFound = dao.queryBuilder()
                                        .where(FavoriteBusStopDao.Properties.StopCode
                                                .eq(favoriteBusStop.getStopCode())).unique();

                                if (favoriteBusStopFound == null) {
                                    dao.insert(favoriteBusStop);
                                    item.setIcon(R.drawable.ic_favorite_outline_white_24dp);
                                } else {
                                    dao.delete(favoriteBusStopFound);
                                    new ToastHelper(getActivity()).showInformation(
                                            "O ponto de parada " + favoriteBusStopFound.getStopCode() +
                                                    " foi removido de seus favoritos.");
                                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                                }

                                dialog.hide();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.hide();
                                new ToastHelper(getActivity()).showError(error.getLocalizedMessage());
                            }
                        }
                );

                ApplicationSingleton.getInstance().addToRequestQueue(request,
                        TAG_REQUEST);

                break;
        }

        return false;
    }

}
