package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import java.util.Locale;

/**
 * Interface utilitária que contém todas constantes comuns ao projeto.
 *
 * @author Filipe Bezerra
 * @since 1.0
 */
public interface ConstantsUtils {

    /**
     * Texto para criação de logs do aplicativo.
     */
    String TAG = "mx.x10.filipebezerra.HorariosRmtcGoiania";

    /**
     * Localização padrão do aplicativo, configurada para pt_BR, ou seja, Português Brasileiro.<br />
     * Esta localização é base para formatação de datas e textos.
     */
    Locale DEFAULT_LOCALE = new Locale("pt", "BR");

    /**
     * Tipo da fonte padrão do aplicativo. A fonte Roboto por padrao somente esta disponivel desde
     * a versao 16 (Jelly Bean).
     */
    String DEFAULT_TYPEFACE = "fonts/Ubuntu-B.ttf";
}
