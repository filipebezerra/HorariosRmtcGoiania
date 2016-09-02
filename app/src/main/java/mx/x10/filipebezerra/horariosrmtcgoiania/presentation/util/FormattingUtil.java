package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util;

import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Filipe Bezerra
 */
public class FormattingUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static final Locale BRAZIL_LOCALE = new Locale("pt", "BR");

    private static final SimpleDateFormat DATE_FORMATTER
            = new SimpleDateFormat(DATE_FORMAT, BRAZIL_LOCALE);

    private FormattingUtil() {}

    public static CharSequence formatAsRelativeDateFromNow(String dateString) {
        try {
            final Date date = DATE_FORMATTER.parse(dateString);
            return DateUtils.getRelativeTimeSpanString(
                    date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        } catch (ParseException e) {
            return dateString;
        }
    }
}
