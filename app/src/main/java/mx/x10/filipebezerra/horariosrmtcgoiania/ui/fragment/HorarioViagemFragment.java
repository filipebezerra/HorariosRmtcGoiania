package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.common.primitives.Ints;
import com.squareup.otto.Bus;

import butterknife.OnClick;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.PersistenceEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.PersistenceMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.SQLitePersistenceMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.parser.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * @author Filipe Bezerra
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    private static final String LOG_TAG = HorarioViagemFragment.class.getSimpleName();

    public static final String ARG_PARAM_BUS_STOP_NUMBER = "BUS_STOP_NUMBER";

    private Bus mEventBus;
    
    public HorarioViagemFragment() {
    }

    public static HorarioViagemFragment newInstance(final Bundle arguments) {
        HorarioViagemFragment fragment = new HorarioViagemFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected String getUrlToLoad() {
        String busStopNumber = null;
        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER)) {
                busStopNumber = arguments.getString(ARG_PARAM_BUS_STOP_NUMBER);
            }
        }

        return busStopNumber == null ? getString(R.string.url_rmtc_horarios_viagem) :
                String.format(getString(R.string.formatted_url_rmtc_horarios_viagem),
                        getString(R.string.url_rmtc_horarios_viagem),
                        getString(R.string.resource_visualizar_ponto), busStopNumber);
    }

    @Override
    protected void setViewStateForPageStarted() {
        super.setViewStateForPageStarted();
        mButtonFloat.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void setViewStateForPageFinished() {
        super.setViewStateForPageFinished();
        
        if (isPreviewPagePoint()) {
            mButtonFloat.setVisibility(View.VISIBLE);

            final SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                    getActivity(), SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);

            final FavoriteBusStop favoriteBusStop = ApplicationSingleton.getInstance()
                    .getDaoSession().getFavoriteBusStopDao().queryBuilder()
                    .where(FavoriteBusStopDao.Properties.StopCode.eq(getBusStopSearched())).unique();

            if (favoriteBusStop != null) {
                mButtonFloat.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_favorite_white_24dp));

                suggestions.saveRecentQuery(
                        String.valueOf(favoriteBusStop.getStopCode()),
                        favoriteBusStop.getAddress());
            } else {

                final ProgressDialog dialog = new ProgressDialog(getActivity(), "Salvando...",
                        R.color.progressBarBackground);
                // TODO : make cancelable
                dialog.setCancelable(false);
                dialog.show();

                final String currentUrl = getWebView().getUrl();

                StringUTF8Request request = new StringUTF8Request(currentUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                FavoriteBusStop favoriteBusStop = new BusStopHtmlParser()
                                        .parse(result);

                                suggestions.saveRecentQuery(
                                        String.valueOf(favoriteBusStop.getStopCode()),
                                        favoriteBusStop.getAddress());

                                dialog.hide();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.hide();
                                Log.e(LOG_TAG, String.format(
                                        "Request for url %s", currentUrl), error);
                            }
                        }
                );

                RequestQueueManager.getInstance(getActivity()).addToRequestQueue(request,
                        LOG_TAG);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonFloat.setBackgroundColor(getResources().getColor(
                R.color.floatingActionButtonBackground));
        mEventBus = EventBusProvider.getInstance().getEventBus();
    }

    @OnClick(R.id.floatingActionButton)
    public void markFavorite() {
        if (!isPreviewPagePoint()) {
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity(), "Pesquisando...",
                R.color.progressBarBackground);
        // TODO : make cancelable
        dialog.setCancelable(false);
        dialog.show();

        final FavoriteBusStopDao dao = ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao();

        FavoriteBusStop favoriteBusStopFound = dao.queryBuilder()
                .where(FavoriteBusStopDao.Properties.StopCode
                        .eq(getBusStopSearched())).unique();

        if (favoriteBusStopFound != null) {
            dialog.setTitle("Removendo...");
            dao.delete(favoriteBusStopFound);

            mEventBus.post(new PersistenceEvent(new SQLitePersistenceMessage(
                    PersistenceMessage.PersistenceType.DELETION, favoriteBusStopFound)));

            dialog.hide();

            animate(mButtonFloat).setInterpolator(new BounceInterpolator())
                    .translationYBy(-34).start();

            SnackBarHelper.show(getActivity(), "Ponto removido de seus favoritos.");

            mButtonFloat.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_favorite_outline_white_24dp));
        } else {
            dialog.setTitle("Adicionando...");
            final String currentUrl = getWebView().getUrl();

            StringUTF8Request request = new StringUTF8Request(currentUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            FavoriteBusStop newFavoriteBusStop = new BusStopHtmlParser()
                                    .parse(result);

                            dao.insert(newFavoriteBusStop);

                            mEventBus.post(new PersistenceEvent(new SQLitePersistenceMessage(
                                    PersistenceMessage.PersistenceType.INSERTION,
                                    newFavoriteBusStop)));                            

                            dialog.hide();

                            animate(mButtonFloat).setInterpolator(new BounceInterpolator())
                                    .translationYBy(-34).start();

                            SnackBarHelper.show(getActivity(), "Ponto marcado como favorito.");

                            mButtonFloat.setDrawableIcon(getResources().getDrawable(
                                    R.drawable.ic_favorite_white_24dp));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.hide();
                            Log.e(LOG_TAG, String.format("Error string request of", currentUrl),
                                    error);
                            SnackBarHelper.show(getActivity(),
                                    "Não foi possível carregar os resultados. Por favor, " +
                                            "Tente novamente!");
                        }
                    }
            );

            RequestQueueManager.getInstance(getActivity()).addToRequestQueue(request,
                    LOG_TAG);
        }

    }

    private boolean isPreviewPagePoint() {
        // TODO : remove usage of Ints from guava lib
        Integer busStopCode = Ints.tryParse(getUrlPartFromCurrentUrl(UrlPart.LAST_PATH_SEGMENT));
        Bundle mArguments = getArguments();
        return busStopCode != null || mArguments != null && mArguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER);
    }

    private Integer getBusStopSearched() {
        if (!isPreviewPagePoint()) {
            return null;
        }

        Integer busStopCode = Ints.tryParse(getUrlPartFromCurrentUrl(UrlPart.LAST_PATH_SEGMENT));

        return busStopCode != null ? busStopCode : Integer.parseInt(getArguments().getString
                (ARG_PARAM_BUS_STOP_NUMBER));
    }

}