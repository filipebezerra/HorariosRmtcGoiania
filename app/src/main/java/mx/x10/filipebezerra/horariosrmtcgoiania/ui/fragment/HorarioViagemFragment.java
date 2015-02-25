package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringUTF8Request;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.google.common.primitives.Ints;

import butterknife.OnClick;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.FavoriteBusStopPersistenceEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.parser.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import static mx.x10.filipebezerra.horariosrmtcgoiania.event.FavoriteBusStopPersistenceEvent
        .PersistenceOperationType.INSERTION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.event.FavoriteBusStopPersistenceEvent
        .PersistenceOperationType.DELETION;

/**
 * @author Filipe Bezerra
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    private static final String ARG_PARAM_BUS_STOP_NUMBER = "BUS_STOP_NUMBER";

    private static final String TAG_REQUEST = "tagStringRequest_"
            + String.valueOf(System.currentTimeMillis());

    private Bundle mArguments;

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
        mArguments = getArguments();

        if (mArguments != null) {
            if (mArguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER)) {
                busStopNumber = mArguments.getString(ARG_PARAM_BUS_STOP_NUMBER);
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

            FavoriteBusStop favoriteBusStop = ApplicationSingleton.getInstance().getDaoSession()
                    .getFavoriteBusStopDao()
                    .queryBuilder().where(FavoriteBusStopDao.Properties.StopCode
                            .eq(getBusStopSearched())).unique();

            if (favoriteBusStop != null) {
                mButtonFloat.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_favorite_white_24dp));
            }
        }
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
        mButtonFloat.setBackgroundColor(getResources().getColor(R.color.floatingActionButtonBackground));
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

            ApplicationSingleton.getInstance().getEventBus().post(
                    new FavoriteBusStopPersistenceEvent(favoriteBusStopFound, DELETION));

            dialog.hide();

            animate(mButtonFloat).setInterpolator(new BounceInterpolator())
                    .translationYBy(-34).start();

            SnackBar snackBar = new SnackBar(getActivity(), "Ponto removido de seus favoritos.");
            snackBar.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    animate(mButtonFloat).setInterpolator(
                            new BounceInterpolator()).translationYBy(44).start();
                }
            });
            snackBar.show();

            mButtonFloat.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_favorite_outline_white_24dp));
        } else {
            dialog.setTitle("Adicionando...");
            String currentUrl = getWebView().getUrl();

            StringUTF8Request request = new StringUTF8Request(currentUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            FavoriteBusStop newFavoriteBusStop = new BusStopHtmlParser()
                                    .parse(result);

                            dao.insert(newFavoriteBusStop);

                            ApplicationSingleton.getInstance().getEventBus().post(
                                    new FavoriteBusStopPersistenceEvent(newFavoriteBusStop, INSERTION));

                            dialog.hide();

                            animate(mButtonFloat).setInterpolator(new BounceInterpolator())
                                    .translationYBy(-34).start();

                            SnackBar snackBar = new SnackBar(getActivity(), "Ponto marcado como favorito.");
                            snackBar.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    animate(mButtonFloat).setInterpolator(
                                            new BounceInterpolator()).translationYBy(44).start();
                                }
                            });

                            snackBar.show();

                            mButtonFloat.setDrawableIcon(getResources().getDrawable(
                                    R.drawable.ic_favorite_white_24dp));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.hide();
                            // TODO : make some toast more friendly
                            new ToastHelper(getActivity()).showError(error.getLocalizedMessage());
                        }
                    }
            );

            ApplicationSingleton.getInstance().addToRequestQueue(request,
                    TAG_REQUEST);
        }

    }

    private boolean isPreviewPagePoint() {
        Integer busStopCode = Ints.tryParse(getUrlPartFromCurrentUrl(UrlPart.LAST_PATH_SEGMENT));
        return busStopCode != null || mArguments != null && mArguments.containsKey(ARG_PARAM_BUS_STOP_NUMBER);
    }

    private Integer getBusStopSearched() {
        if (!isPreviewPagePoint()) {
            return null;
        }

        Integer busStopCode = Ints.tryParse(getUrlPartFromCurrentUrl(UrlPart.LAST_PATH_SEGMENT));

        return busStopCode != null ? busStopCode : Integer.parseInt(mArguments.getString
                (ARG_PARAM_BUS_STOP_NUMBER));
    }

}
