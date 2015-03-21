package mx.x10.filipebezerra.horariosrmtcgoiania.managers;

import android.content.Context;
import android.provider.SearchRecentSuggestions;

import mx.x10.filipebezerra.horariosrmtcgoiania.providers.SuggestionsProvider;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 10/03/2015
 * @since #
 */
public class SuggestionsProviderManager {
    private static SuggestionsProviderManager mInstance;
    private final SearchRecentSuggestions mSuggestionsProvider;

    private SuggestionsProviderManager(Context context) {
        mSuggestionsProvider = new SearchRecentSuggestions(
                context, SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
    }

    public static synchronized SuggestionsProviderManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SuggestionsProviderManager(context);
        }
        return  mInstance;
    }

    public void saveQuery(final String query, final String queryLine2) {
        mSuggestionsProvider.saveRecentQuery(query, queryLine2);
    }
}
