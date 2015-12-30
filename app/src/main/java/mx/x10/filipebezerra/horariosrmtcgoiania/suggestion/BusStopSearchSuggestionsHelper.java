package mx.x10.filipebezerra.horariosrmtcgoiania.suggestion;

import android.content.Context;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;

import static mx.x10.filipebezerra.horariosrmtcgoiania.suggestion.BusStopSearchSuggestionsProvider.AUTHORITY;
import static mx.x10.filipebezerra.horariosrmtcgoiania.suggestion.BusStopSearchSuggestionsProvider.MODE;

/**
 * {@link BusStopSearchSuggestionsProvider} helper class.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 01/12/2015
 * @since 0.1.0
 */
public class BusStopSearchSuggestionsHelper {
    private static SearchRecentSuggestions sSearchRecentSuggestions;

    private BusStopSearchSuggestionsHelper() {
        // no instances
    }

    public static void saveSuggestion(@NonNull Context context,
            @NonNull String firstLine, @NonNull String secondLine) {
        instance(context).saveRecentQuery(firstLine, secondLine);
    }

    public static void clearSuggestions(@NonNull Context context) {
        instance(context).clearHistory();
    }

    private static SearchRecentSuggestions instance(@NonNull Context context) {
        if (sSearchRecentSuggestions == null) {
            sSearchRecentSuggestions = new SearchRecentSuggestions(context, AUTHORITY, MODE);
        }
        return sSearchRecentSuggestions;
    }
}
