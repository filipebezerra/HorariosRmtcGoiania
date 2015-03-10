package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFloat;

import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.makeLogTag;

/**
 * Fragment composed by a {@link android.webkit.WebView}, an animated
 * {@link com.gc.materialdesign.views.ProgressBarCircularIndeterminate} and a special action
 * {@link com.gc.materialdesign.views.ButtonFloat} based in Material design.
 *
 * @author Filipe Bezerra
 * @version 2.0, 09/03/2015
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.BaseWebViewFragment
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {
    private static final String LOG_TAG = makeLogTag(HorarioViagemFragment.class);

    public static final String ARG_PARAM_BUS_STOP_CODE = BaseWebViewFragment.class.getSimpleName()
            + "ARG_PARAM_BUS_STOP_CODE";

    private boolean mIsViewingBusStopPage = false;

    @InjectView(R.id.floatButtonMarkFavorite) protected ButtonFloat mFloatButtonMarkFavorite;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (getArgBusStopCode() != null) {
            mIsViewingBusStopPage = true;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return setupContentView(inflater.inflate(R.layout.fragment_horario_viagem, container, false));
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_update_bus_travel_times, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                if (mSwipeRefreshLayout != null && ! mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                initiateReloading();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View setupContentView(final View fragmentView) {
        super.setupContentView(fragmentView);
        mFloatButtonMarkFavorite.setBackgroundColor(getResources().getColor(
                R.color.floating_action_button_background));
        return fragmentView;
    }

    @Override
    public void onWebViewPageStarted() {
        super.onWebViewPageStarted();
        if (mFloatButtonMarkFavorite != null) {
            mFloatButtonMarkFavorite.setVisibility(View.GONE);
            mFloatButtonMarkFavorite.hide();
        }
    }

    @Override
    public void onWebViewPageFinished() {
        super.onWebViewPageFinished();

        if (mFloatButtonMarkFavorite != null) {
            if (!mIsViewingBusStopPage && getBusStopCodeFromCurrentUrl() != null) {
                mIsViewingBusStopPage = true;
            }

            if (getWebView() != null) {
                if (mIsViewingBusStopPage) {
                    mFloatButtonMarkFavorite.setVisibility(View.VISIBLE);
                    mFloatButtonMarkFavorite.show();
                }
            }
        }
    }

    /**
     * Returns the bus stop code passed to arguments in the helper constructor.
     *
     * @return bus stop code passed to arguments
     */
    public String getArgBusStopCode() {
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_PARAM_BUS_STOP_CODE)) {
                return getArguments().getString(ARG_PARAM_BUS_STOP_CODE);
            }
        }
        return null;
    }

    public Integer getBusStopCodeFromCurrentUrl() {
        try {
            final String lastPathSegment = Uri.parse(getWebView().getUrl()).getLastPathSegment();
            return Integer.parseInt(lastPathSegment);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    /*
    @Override
    protected void onWebViewPageFinished() {
        super.onWebViewPageFinished();

        // TODO : fix and improve this. At present raise NullPointerException calling mAttachedActivity
        
        if (isPreviewPagePoint()) {
            mFloatButtonMarkFavorite.setVisibility(View.VISIBLE);
            mFloatButtonMarkFavorite.show();

            /**
            final FavoriteBusStop favoriteBusStop = ApplicationSingleton.getInstance()
                    .getDaoSession().getFavoriteBusStopDao().queryBuilder()
                    .where(FavoriteBusStopDao.Properties.StopCode.eq(getBusStopSearched())).unique();

            if (favoriteBusStop != null) {
                mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_drawer_pontos_favoritos));
            } else {

                /*
                final ProgressDialog dialog = new ProgressDialog(mAttachedActivity, "Salvando...",
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

                                //dialog.hide();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //dialog.hide();
                                LOGE(LOG_TAG, String.format(
                                        getString(R.string.log_error_network_request),
                                        error.getClass().toString(), "onErrorResponse", "String",
                                        currentUrl), error);
                                SnackBarHelper.show(mAttachedActivity,
                                        getString(R.string.error_in_network_search_request));
                            }
                        }
                );

                RequestQueueManager.getInstance(mAttachedActivity).addToRequestQueue(request,
                        LOG_TAG);
            }
        }
    }

    @OnClick(R.id.floatButtonMarkFavorite)
    public void markFavorite() {
        if (!isPreviewPagePoint()) {
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(mAttachedActivity, "Pesquisando...",
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


            dialog.hide();

            // TODO : Animate to avoid SnackBar blocking the Floating Button
            //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator())
                    //.translationYBy(-34).start();

            SnackBarHelper.show(mAttachedActivity, "Ponto removido de seus favoritos.");

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


                            dialog.hide();

                            // TODO : Animate to avoid SnackBar blocking the Floating Button
                            //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator())
                                    //.translationYBy(-34).start();

                            SnackBarHelper.show(mAttachedActivity, "Ponto marcado como favorito.");

                            mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                                    R.drawable.ic_drawer_pontos_favoritos));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.hide();
                            LOGE(LOG_TAG, String.format(
                                    getString(R.string.log_error_network_request),
                                    error.getClass().toString(), "onErrorResponse", "String",
                                    currentUrl), error);
                            SnackBarHelper.show(mAttachedActivity,
                                    getString(R.string.error_in_network_search_request_parsing_html_page));
                        }
                    }
            );

            RequestQueueManager.getInstance(mAttachedActivity).addToRequestQueue(request,
                    LOG_TAG);
        }

    }
    */
}