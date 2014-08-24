package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Classe utilitária responsável por enviar notificações para a interface de usuário.
 *
 * @author Filipe Bezerra
 * @since 1.4-m2
 */
public final class NotificationUtils {
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
