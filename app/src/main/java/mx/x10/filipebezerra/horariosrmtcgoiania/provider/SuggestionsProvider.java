package mx.x10.filipebezerra.horariosrmtcgoiania.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class SuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = SuggestionsProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}