package mx.x10.filipebezerra.horariosrmtcgoiania.android.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import mx.x10.filipebezerra.horariosrmtcgoiania.linesavailable.LinesAvailableActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.webservice.BusStation;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class ActivityNavigator {
    public static void navigateToLinesAvailable(@NonNull Context context,
            @NonNull BusStation busStation) {
        context.startActivity(
                new Intent(context, LinesAvailableActivity.class)
                        .putExtra(LinesAvailableActivity.EXTRA_BUS_STATION, busStation)
        );
    }
}
