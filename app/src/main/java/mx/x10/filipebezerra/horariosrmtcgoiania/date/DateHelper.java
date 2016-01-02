package mx.x10.filipebezerra.horariosrmtcgoiania.date;

import android.support.annotation.NonNull;
import java.text.ParseException;

import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.API_DATE_TIME_FORMATTER;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.DEFAULT_TIME_FORMATTER;

/**
 * Date and time utility methods.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 02/01/2016
 * @since 3.0.0
 */
@SuppressWarnings("ConstantConditions")
public class DateHelper {
    private DateHelper() {
        // no instances
    }

    public static String toTimeFormat(@NonNull String dateString) {
        if (dateString == null) {
            throw new NullPointerException("dateString cannot be null.");
        }

        try {
            return DEFAULT_TIME_FORMATTER.format(
                    API_DATE_TIME_FORMATTER.parse(dateString));
        } catch (ParseException e) {
            return dateString;
        }
    }
}
