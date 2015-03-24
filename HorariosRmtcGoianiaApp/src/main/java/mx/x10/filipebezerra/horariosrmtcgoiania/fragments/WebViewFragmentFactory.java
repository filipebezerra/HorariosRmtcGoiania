package mx.x10.filipebezerra.horariosrmtcgoiania.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 09/03/2015
 * @since #
 */
public class WebViewFragmentFactory {
    /**
     * Helper method to build argumens with a url page.
     *
     * @param url the base url page
     * @param paths aditional paths increased at the final of the url
     * @return collection with the url page argument.
     */
    public static Bundle buildFinalUrl(final String url, String...paths) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String path : paths) {
            builder.appendEncodedPath(path);
        }
        final String finalUrl = builder.build().toString();

        Bundle arguments = new Bundle();
        arguments.putString(BaseWebViewFragment.ARG_PARAM_URL_PAGE, finalUrl);
        return arguments;
    }

    /**
     * Factory method for a WebView fragment within a custom page.
     *
     * @param url the url base page
     * @param paths aditional paths increased at the final of the url
     * @return a new instance of BaseWebViewFragment
     */
    public static BaseWebViewFragment newBaseWebViewFragment(final String url, String...paths) {
        Bundle arguments = buildFinalUrl(url, paths);
        BaseWebViewFragment webViewFragment = new BaseWebViewFragment();
        webViewFragment.setArguments(arguments);
        return webViewFragment;
    }

    /**
     * Factory method for a WebView fragment within a WAP page.
     */
    public static BaseWebViewFragment newWapPageFragment(Context context) {
        return newBaseWebViewFragment(context.getString(R.string.url_rmtc_wap));
    }

    /**
     * Factory method for a WebView fragment within a Planeje sua Viagem page.
     */
    public static BaseWebViewFragment newPlanejeViagemPageFragment(Context context) {
        return newBaseWebViewFragment(context.getString(R.string.url_rmtc_planeje_viagem));
    }

    /**
     * Factory method for a WebView fragment within a Ponto a Ponto page.
     */
    public static BaseWebViewFragment newPontoaPontoPageFragment(Context context) {
        return newBaseWebViewFragment(context.getString(R.string.url_rmtc_ponto_a_ponto));
    }

    /**
     * Factory method for a WebView fragment within a SAC page.
     */
    public static BaseWebViewFragment newSacPageFragment(Context context) {
        return newBaseWebViewFragment(context.getString(R.string.url_rmtc_sac));
    }

    /**
     * Factory method for a WebView fragment within a Horário de Viagem page.
     */
    public static HorarioViagemFragment newHorarioViagemPageFragment(Context context) {
        HorarioViagemFragment horarioViagemFragment = new HorarioViagemFragment();
        horarioViagemFragment.setArguments(buildFinalUrl(
                context.getString(R.string.url_rmtc_horario_viagem)));
        return horarioViagemFragment;
    }

    /**
     * Factory method for a WebView fragment within a Horário de Viagem page referring to the
     * timetable of a specific point.
     *
     * @param context context
     * @param busStopCode the bus stop code argument
     */
    public static HorarioViagemFragment newHorarioViagemPageFragment(Context context,
                                                                     String busStopCode) {
        HorarioViagemFragment horarioViagemFragment = new HorarioViagemFragment();
        Bundle arguments = buildHorarioViagemUrl(context, busStopCode);
        horarioViagemFragment.setArguments(arguments);
        return horarioViagemFragment;
    }

    /**
     * Helper method to build argumens to {@link mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment}.
     *
     * @param context context
     * @param busStopCode the bus stop code argument
     * @return collection with the url page argument.
     */
    public static Bundle buildHorarioViagemUrl(final Context context, final String busStopCode) {
        Bundle arguments = buildFinalUrl(context.getString(R.string.url_rmtc_horario_viagem),
                context.getString(R.string.url_path_visualizar_ponto), busStopCode);
        arguments.putString(HorarioViagemFragment.ARG_PARAM_BUS_STOP_CODE, busStopCode);
        return arguments;
    }
}
