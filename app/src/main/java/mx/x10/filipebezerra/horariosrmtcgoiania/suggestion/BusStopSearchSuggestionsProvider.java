package mx.x10.filipebezerra.horariosrmtcgoiania.suggestion;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Search suggestions provider used in searches by bus stop.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 01/11/2015
 * @since 0.1.0
 */
public class BusStopSearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = BusStopSearchSuggestionsProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public BusStopSearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
