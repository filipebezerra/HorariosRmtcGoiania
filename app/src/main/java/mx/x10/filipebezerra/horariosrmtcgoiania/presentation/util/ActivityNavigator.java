package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus.LinhasOnibusActivity;

/**
 * @author Filipe Bezerra
 */
public class ActivityNavigator {
    private ActivityNavigator() {}

    public static void navigateToLinhasOnibusActivity(
            @NonNull Context context,
            @NonNull String numeroPonto) {
        context.startActivity(
                new Intent(context, LinhasOnibusActivity.class)
                        .putExtra(LinhasOnibusActivity.EXTRA_NUMERO_PONTO, numeroPonto));
    }
}
