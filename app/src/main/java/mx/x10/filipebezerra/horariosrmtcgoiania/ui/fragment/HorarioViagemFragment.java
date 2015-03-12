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
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.otto.Bus;

import butterknife.InjectView;
import butterknife.OnClick;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.parser.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ProgressDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGD;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGE;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.makeLogTag;

/**
 * Fragment composed by a {@link android.webkit.WebView}, an animated
 * {@link com.gc.materialdesign.views.ProgressBarCircularIndeterminate} and a special action
 * {@link com.gc.materialdesign.views.ButtonFloat} based in Material design.
 *
 * @author Filipe Bezerra
 * @version 2.0, 10/03/2015
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.BaseWebViewFragment
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {
    private static final String LOG_TAG = makeLogTag(HorarioViagemFragment.class);

    public static final String ARG_PARAM_BUS_STOP_CODE = BaseWebViewFragment.class.getSimpleName()
            + "ARG_PARAM_BUS_STOP_CODE";

    private boolean mIsViewingBusStopPage = false;

    @InjectView(R.id.floatButtonMarkFavorite) protected ButtonFloat mFloatButtonMarkFavorite;

    private Bus mEventBus;

    /**
     *
     */
    private FavoriteBusStopDao mFavoriteBusStopDao;

    /**
     * If {@link #mIsViewingBusStopPage} then in {@link #onWebViewPageFinished} will try to retrieve
     * the data associated with the bus stop code calling {@link #getArgBusStopCode} if was explicitly
     * passed into {@link android.support.v4.app.Fragment#setArguments(android.os.Bundle)} or
     * calling {@link #getBusStopCodeFromCurrentUrl} if was a search using the webview fields.
     */
    private FavoriteBusStop mPersistedFavoriteBusStop;

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
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavoriteBusStopDao = DaoManager.getInstance(mAttachedActivity).getFavoriteBusStopDao();
        mEventBus = EventBusProvider.getInstance().getEventBus();
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
            mFloatButtonMarkFavorite.setEnabled(false);
        }
    }

    public boolean isViewingBusStopPage() {
        return mIsViewingBusStopPage;
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
                    mFloatButtonMarkFavorite.setEnabled(true);

                    mPersistedFavoriteBusStop = getPersistedFavoriteBusStop();
                    LOGD(LOG_TAG, String.format(getString(R.string.log_event_debug),
                            "onWebViewPageFinished", "persisted favorite bus stop",
                            mPersistedFavoriteBusStop == null ? "but it\'s not" :
                                    "is persisted with id: "+mPersistedFavoriteBusStop.getId()));

                    if (mPersistedFavoriteBusStop != null) {
                        mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                                R.drawable.ic_drawer_pontos_favoritos));
                    }
                }
            }
        }
    }

    /**
     * Returns the bus stop code passed to arguments in the helper constructor.
     *
     * @return bus stop code passed to arguments
     */
    public Integer getArgBusStopCode() {
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_PARAM_BUS_STOP_CODE)) {
                try {
                    return Integer.parseInt(getArguments().getString(ARG_PARAM_BUS_STOP_CODE));
                } catch (NumberFormatException e) {
                    return null;
                }
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

    public void reloadPageFromArguments(@Nullable final Bundle newPageArgs) {
        WebView webView = getWebView();

        if (webView == null)
            return;

        if (newPageArgs == null) {
            webView.loadUrl(getArguments().getString(ARG_PARAM_URL_PAGE));
        } else {
            webView.loadUrl(newPageArgs.getString(ARG_PARAM_URL_PAGE));
        }
    }

    @OnClick(R.id.floatButtonMarkFavorite)
    public void markFavorite() {
        if (! mIsViewingBusStopPage) {
            return;
        }

        if (mPersistedFavoriteBusStop == null) {
            final WebView webView = getWebView();
            if (webView != null) {
                final String currentUrl = webView.getUrl();

                ProgressDialogHelper.show(mAttachedActivity, "Adicionando, por favor aguarde...",
                        ((MaterialNavigationDrawer) mAttachedActivity).getCurrentSection()
                                .getSectionColor());

                StringUTF8Request request = new StringUTF8Request(currentUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                FavoriteBusStop newFavoriteBusStop = new BusStopHtmlParser()
                                        .parse(result);

                                mFavoriteBusStopDao.insert(newFavoriteBusStop);
                                mPersistedFavoriteBusStop = newFavoriteBusStop;

                                mEventBus.post(new NotificationEvent(new NotificationMessage(
                                        NotificationMessage.NotificationType.INCREMENT)));

                                ProgressDialogHelper.dismiss();

                                // TODO : Animate to avoid SnackBar blocking the Floating Button
                                //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator()).translationYBy(-34).start();

                                SnackBarHelper.show(mAttachedActivity, "Ponto marcado como favorito.");

                                mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                                        R.drawable.ic_drawer_pontos_favoritos));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ProgressDialogHelper.dismiss();
                                LOGE(LOG_TAG, String.format(
                                        getString(R.string.log_event_error_network_request),
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
        } else {
            ProgressDialogHelper.show(mAttachedActivity, "Removendo, por favor aguarde...",
                    ((MaterialNavigationDrawer) mAttachedActivity).getCurrentSection()
                            .getSectionColor());
            mFavoriteBusStopDao.delete(mPersistedFavoriteBusStop);
            mPersistedFavoriteBusStop = null;

            mEventBus.post(new NotificationEvent(new NotificationMessage(
                    NotificationMessage.NotificationType.DECREMENT)));
            ProgressDialogHelper.dismiss();

            // TODO : Animate to avoid SnackBar blocking the Floating Button
            //animate(mFloatButtonMarkFavorite).setInterpolator(new BounceInterpolator()).translationYBy(-34).start();

            SnackBarHelper.show(mAttachedActivity, "Ponto removido de seus favoritos.");
            mFloatButtonMarkFavorite.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_unmark_favorite));
        }
    }

    private FavoriteBusStop getPersistedFavoriteBusStop() {
        if (mIsViewingBusStopPage) {
            Integer busStopCode = getArgBusStopCode();

            if (busStopCode == null) {
                busStopCode = getBusStopCodeFromCurrentUrl();
            }

            return mFavoriteBusStopDao.queryBuilder()
                    .where(FavoriteBusStopDao.Properties.StopCode.eq(busStopCode))
                    .unique();
        } else
            return null;
    }
}