package mx.x10.filipebezerra.horariosrmtcgoiania.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import de.psdev.licensesdialog.LicensesDialog;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.NotificationMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.DialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SearchRecentSuggestionsHelper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_ABOUT_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CHANGELOG_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CLEAR_FAVORITE_BUS_STOP_DATA;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_CLEAR_RECENT_SUGGESTIONS;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_OPEN_SOURCE_LICENSES_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils.PREF_PRODUCT_TOUR;

/**
 * Preference screen for app preferences.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class SettingsActivity extends PreferenceActivity
        implements View.OnClickListener, Preference.OnPreferenceClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();
        setupPreferenceVIew();
    }

    private void setupPreferenceVIew() {
        addPreferencesFromResource(R.xml.preferences);
        findPreference(PREF_CLEAR_RECENT_SUGGESTIONS).setOnPreferenceClickListener(this);
        findPreference(PREF_CLEAR_FAVORITE_BUS_STOP_DATA).setOnPreferenceClickListener(this);
        findPreference(PREF_ABOUT_INFO).setOnPreferenceClickListener(this);
        findPreference(PREF_OPEN_SOURCE_LICENSES_INFO).setOnPreferenceClickListener(this);
        findPreference(PREF_CHANGELOG_INFO).setOnPreferenceClickListener(this);
        findPreference(PREF_PRODUCT_TOUR).setOnPreferenceClickListener(this);
    }

    private void setupContentView() {
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_settings));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final String key = preference.getKey();
        MaterialDialog.Builder dialogBuilder;

        switch (key) {
            case PREF_CLEAR_RECENT_SUGGESTIONS:
                dialogBuilder = DialogHelper.build(SettingsActivity.this,
                        R.string.pref_clear_recent_suggestions_title,
                        R.string.prompt_user_pref_clear_recent_suggestions,
                        R.string.erase_button,
                        R.string.cancel_button);
                dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        SearchRecentSuggestionsHelper.getInstance(SettingsActivity.this)
                                .clearHistory();
                        preference.setSummary(getString(R.string
                                .pref_clear_recent_suggestions_summary_edited));
                        preference.setEnabled(false);
                    }
                });
                dialogBuilder.show();
                return true;

            case PREF_CLEAR_FAVORITE_BUS_STOP_DATA:
                dialogBuilder = DialogHelper.build(SettingsActivity.this,
                        R.string.pref_clear_favorite_bus_stop_data_title,
                        R.string.prompt_user_pref_clear_favorite_bus_stop_data,
                        R.string.erase_button,
                        R.string.cancel_button);
                dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        DaoManager.getInstance(SettingsActivity.this).getFavoriteBusStopDao()
                                .deleteAll();
                        preference.setSummary(getString(R.string
                                .pref_clear_favorite_bus_stop_data_summary_edited));
                        preference.setEnabled(false);

                        EventBusProvider.getInstance().getEventBus().post(
                                new NotificationEvent(new NotificationMessage(
                                        NotificationMessage.NotificationType.RESET)));
                    }
                });
                dialogBuilder.show();
                return true;

            case PREF_ABOUT_INFO:
                DialogHelper.build(SettingsActivity.this,
                        R.string.pref_about_info_key_title,
                        R.string.dialog_message_about_info,
                        R.string.ok_button).show();
                return true;

            case PREF_OPEN_SOURCE_LICENSES_INFO:
                new LicensesDialog.Builder(this).setNotices(R.raw.notices)
                        .setThemeResourceId(R.style.Widget_LicensesDialog)
                        .setTitle(getString(R.string.pref_open_source_licenses_info_key_title))
                        .setDividerColorId(R.color.licenses_dialog_divider_color).build().show();
                return true;

            case PREF_CHANGELOG_INFO:
                dialogBuilder = DialogHelper.build(SettingsActivity.this,
                        R.string.pref_changelog_info_title,
                        R.string.dialog_message_about_info,
                        R.string.ok_button);
                dialogBuilder.customView(R.layout.view_changelog, false).show();
                return true;

            case PREF_PRODUCT_TOUR:
                return false;
        }
        return false;
    }
}