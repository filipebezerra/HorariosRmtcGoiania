package mx.x10.filipebezerra.horariosrmtcgoiania.providers;

import android.content.SearchRecentSuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;

/**
 * @author Filipe Bezerra
 * @version 2.3.1, 17/01/2016
 * @since 2.0
 */
public class SuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = BuildConfig.DEBUG ?
            "mx.x10.filipebezerra.horariosrmtcgoiania.dev.providers.SuggestionsProvider" :
            "mx.x10.filipebezerra.horariosrmtcgoiania.providers.SuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
    
}