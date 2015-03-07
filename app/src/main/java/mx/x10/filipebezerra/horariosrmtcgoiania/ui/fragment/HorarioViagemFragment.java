package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.gc.materialdesign.widgets.ProgressDialog;
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
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGD;

/**
 * Fragment composed by a {@link android.webkit.WebView}, an animated
 * {@link com.gc.materialdesign.views.ProgressBarCircularIndeterminate} and a special action
 * {@link com.gc.materialdesign.views.ButtonFloat} based in Material design.
 *
 * @author Filipe Bezerra
 * @version 2.0, 06/03/2015
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.BaseWebViewFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    private static final String LOG_TAG = HorarioViagemFragment.class.getSimpleName();

    public static final String ARG_PARAM_BUS_STOP_NUMBER = "BUS_STOP_NUMBER";

    private Bus mEventBus;

    public HorarioViagemFragment() {
        setArguments(Bundle.EMPTY);
        LOGD(LOG_TAG, this.getClass().getSimpleName() + " created1");
    }

    public static HorarioViagemFragment newInstance(final String singleArgument) {
        HorarioViagemFragment fragment = new HorarioViagemFragment();
        
        if (TextUtils.isEmpty(singleArgument)) {
            fragment.setArguments(Bundle.EMPTY);
        } else {
            Bundle arguments = new Bundle(1);
            arguments.putString(ARG_PARAM_BUS_STOP_NUMBER, singleArgument);
            fragment.setArguments(arguments);
        }
        
        return fragment;
    }
    
    public static HorarioViagemFragment newInstance(final Bundle arguments) {
        HorarioViagemFragment fragment = new HorarioViagemFragment();
        fragment.setArguments(arguments == null ? Bundle.EMPTY : arguments);
        return fragment;
    }

    @Override
    protected String getUrlToLoad() {
        String busStopNumber = null;
        Bundle arguments = getArguments();

        if (!Bundle.EMPTY.equals(arguments)) {
            if (arguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER)) {
                busStopNumber = arguments.getString(ARG_PARAM_BUS_STOP_NUMBER);
            }
        }

        return busStopNumber == null ? getString(R.string.url_rmtc_horarios_viagem) :
                String.format(getString(R.string.url_formatted_rmtc_horarios_viagem),
                        getString(R.string.url_rmtc_horarios_viagem),
                        getString(R.string.url_partial_visualizar_ponto), busStopNumber);
    }

    @Override
    protected void onWebViewPageStarted() {
        super.onWebViewPageStarted();
    }

    @Override
    protected void onWebViewPageFinished() {
        super.onWebViewPageFinished();

        // TODO : fix and improve this. At present raise NullPointerException calling getActivity()
        /*
        if (isPreviewPagePoint()) {
            mFloatButtonMarkFavorite.setVisibility(View.VISIBLE);
            mFloatButtonMarkFavorite.show();

            final SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                    getActivity(), SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);

            final FavoriteBusStop favoriteBusStop = ApplicationSingleton.getInstance()
                    .getDaoSession().getFavoriteBusStopDao().queryBuilder()
                    .where(FavoriteBusStopDao.Properties.StopCode.eq(getBusStopSearched())).unique();

            if (favoriteBusStop != null) {
                mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_drawer_pontos_favoritos));

                suggestions.saveRecentQuery(
                        String.valueOf(favoriteBusStop.getStopCode()),
                        favoriteBusStop.getAddress());
            } else {

                final ProgressDialog dialog = new ProgressDialog(getActivity(), "Salvando...",
                        R.color.progress_bar_background);
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
        */
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFloatButtonMarkFavorite.setBackgroundColor(getResources().getColor(
                R.color.floating_action_button_background));
        mEventBus = EventBusProvider.getInstance().getEventBus();
    }

    @OnClick(R.id.floatButtonMarkFavorite)
    public void markFavorite() {
        if (!isPreviewPagePoint()) {
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity(), "Pesquisando...",
                R.color.progress_bar_background);
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

            // TODO : handle persistentEvents in the appropriate handler
            mEventBus.post(new PersistenceEvent(new SQLitePersistenceMessage(
                    PersistenceMessage.PersistenceType.DELETION, favoriteBusStopFound)));
            
            // TODO : post notificationEvents
            /**
            mEventBus.post(new NotificationEvent(new NotificationMessage(
                    NotificationMessage.NotificationType.DECREMENT)));
             **/

            dialog.hide();

            // TODO : Animate to avoid SnackBar blocking the Floating Button
            //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator())
                    //.translationYBy(-34).start();

            SnackBarHelper.show(getActivity(), "Ponto removido de seus favoritos.");

            mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_unmark_favorite));
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

                            // TODO : handle persistentEvents in the appropriate handler
                            mEventBus.post(new PersistenceEvent(new SQLitePersistenceMessage(
                                    PersistenceMessage.PersistenceType.INSERTION,
                                    newFavoriteBusStop)));

                            // TODO : post notificationEvents
                            /**
                            mEventBus.post(new NotificationEvent(new NotificationMessage(
                                    NotificationMessage.NotificationType.INCREMENT)));
                             **/

                            dialog.hide();

                            // TODO : Animate to avoid SnackBar blocking the Floating Button
                            //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator())
                                    //.translationYBy(-34).start();

                            SnackBarHelper.show(getActivity(), "Ponto marcado como favorito.");

                            mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                                    R.drawable.ic_drawer_pontos_favoritos));
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
        Integer busStopCode = parseBusStopCodeFromCurrentUrl();
        Bundle mArguments = getArguments();
        return busStopCode != null || mArguments != null && mArguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER);
    }
    
    private Integer parseBusStopCodeFromCurrentUrl() {
        String busStopCodeSegment = getUrlPartFromCurrentUrl(UrlPart.LAST_PATH_SEGMENT);
        
        try {
            return TextUtils.isEmpty(busStopCodeSegment) ? null : Integer.parseInt(busStopCodeSegment);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getBusStopSearched() {
        if (!isPreviewPagePoint()) {
            return null;
        }

        Integer busStopCode = parseBusStopCodeFromCurrentUrl();

        return busStopCode != null ? busStopCode : Integer.parseInt(getArguments().getString
                (ARG_PARAM_BUS_STOP_NUMBER));
    }
}