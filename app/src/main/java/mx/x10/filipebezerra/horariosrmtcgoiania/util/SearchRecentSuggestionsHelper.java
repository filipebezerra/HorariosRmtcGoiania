package mx.x10.filipebezerra.horariosrmtcgoiania.util;

import android.content.Context;
import android.provider.SearchRecentSuggestions;

import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 27/02/2015
 * @since #
 */
public final class SearchRecentSuggestionsHelper {
    
    public static SearchRecentSuggestions getInstance(Context context) {
        return new SearchRecentSuggestions(
                context, SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
    }
    
}
