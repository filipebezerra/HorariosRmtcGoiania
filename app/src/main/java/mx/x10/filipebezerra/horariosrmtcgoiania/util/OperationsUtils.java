package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import static android.util.Log.d;

import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;

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
        DEBUG;
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
            }
        }
    }
}
