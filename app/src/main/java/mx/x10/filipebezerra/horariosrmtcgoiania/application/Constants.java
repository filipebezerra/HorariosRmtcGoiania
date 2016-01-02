package mx.x10.filipebezerra.horariosrmtcgoiania.application;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Global application constants.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 30/12/2015
 * @since 3.0.0
 */
public final class Constants {

    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;

    public static final int REQUEST_LOCATION = 1002;

    public static final String API_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final SimpleDateFormat API_DATE_TIME_FORMATTER =
            new SimpleDateFormat(API_DATE_TIME_FORMAT, Locale.getDefault());

    public static final String DEFAULT_TIME_FORMAT = "HH:mm";

    public static final SimpleDateFormat DEFAULT_TIME_FORMATTER =
            new SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault());
}
