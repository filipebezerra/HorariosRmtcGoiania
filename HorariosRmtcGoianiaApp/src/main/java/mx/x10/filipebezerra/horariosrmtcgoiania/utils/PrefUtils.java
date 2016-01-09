package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Utilities and constants related to app preferences..
 *
 * @author Filipe Bezerra
 * @version 2.3, 09/01/2016
 * @since 2.0
 */
public final class PrefUtils {
    /**
     * Preference that triggers onClick listener to clear recent search suggestions from provider.
     */
    public static final String PREF_CLEAR_RECENT_SUGGESTIONS = "pref_clear_recent_suggestions";

    /**
     * Preference that triggers onClick listener to remove favorite bus stop from database.
     */
    public static final String PREF_CLEAR_FAVORITE_BUS_STOP_DATA = "pref_clear_favorite_bus_stop_data";

    /**
     * Preference that triggers onClick listener to show app about dialog.
     */
    public static final String PREF_ABOUT_VERSION = "pref_about_version";

    /**
     * Preference that triggers onClick listener to show open source licences dialog.
     */
    public static final String PREF_LICENSES_INFO = "pref_licenses_info";

    /**
     * Preference that triggers onClick listener to show app changelog.
     */
    public static final String PREF_CHANGELOG_INFO = "pref_changelog_info";

    /**
     * Preference that triggers onClick listener to show product tour guide.
     */
    public static final String PREF_TOUR = "pref_tour";

    /**
     * Boolean indicating whether we performed the (one-time) welcome flow.
     */
    public static final String PREF_WELCOME_DONE = "pref_welcome_done";

    public static final String PREF_MAIL_TO_DEVELOPER = "mail_to_developer";

    public static boolean isWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferencesCompat.EditorCompat.getInstance()
                .apply(sp.edit().putBoolean(PREF_WELCOME_DONE, true));
    }
}
