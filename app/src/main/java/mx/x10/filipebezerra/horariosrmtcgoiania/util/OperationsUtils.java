package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import static android.util.Log.d;
import static android.util.Log.e;

import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 * @since 1.0
 */
public final class OperationsUtils implements ConstantsUtils {

    /**
     * Tipo do log a ser criado.
     */
    public enum LogType {
        /**
         * Depuração, filtro exclusivo para o desenvolvedor.
         */
        DEBUG,

        /**
         * Erro, falhas e bugs
         */
        ERROR;
    }

    /**
     * Criação de logs com mensagem formadata. A mensagem será formatada com o padrão do aplicativo,
     * ou seja, pt_BR.
     *
     * @param logType tipo de log.
     * @param formattedLogMessage mensagem do log com formatação.
     * @param arguments argumentos utilizados na mensagem formatada.
     *
     * @see java.util.Formatter
     */
    public static void log(final LogType logType, final String formattedLogMessage,
                           final Object... arguments) {
        log(logType, String.format(DEF_LOCALE, formattedLogMessage, arguments));
    }

    /**
     * Criação de logs.
     *
     * @param logType tipo de log.
     * @param logMessage mensagem do log.
     */
    public static void log(final LogType logType, final String logMessage) {
        if (BuildConfig.DEBUG) {
            switch(logType) {
                case DEBUG:
                    d(TAG, logMessage);
                    break;
                case ERROR:
                    e(TAG, logMessage);
                    break;
            }
        }
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       Boolean status, int iconRes) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setTitle(title)
                .setIcon(iconRes)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }
}
