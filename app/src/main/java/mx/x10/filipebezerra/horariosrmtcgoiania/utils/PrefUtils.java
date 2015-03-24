package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utilities and constants related to app preferences..
 *
 * @author Filipe Bezerra
 * @version 2, 10/03/2015
 * @since #
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
    public static final String PREF_ABOUT_INFO = "pref_about_info";

    /**
     * Preference that triggers onClick listener to show open source licences dialog.
     */
    public static final String PREF_OPEN_SOURCE_LICENSES_INFO = "pref_open_source_licenses_info";

    /**
     * Preference that triggers onClick listener to show app changelog.
     */
    public static final String PREF_CHANGELOG_INFO = "pref_changelog_info";

    /**
     * Preference that triggers onClick listener to show product tour guide.
     */
    public static final String PREF_PRODUCT_TOUR = "pref_product_tour";

    /**
     * Boolean indicating whether we performed the (one-time) welcome flow.
     */
    public static final String PREF_WELCOME_DONE = "pref_welcome_done";

    public static boolean isWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, true).commit();
    }
}
