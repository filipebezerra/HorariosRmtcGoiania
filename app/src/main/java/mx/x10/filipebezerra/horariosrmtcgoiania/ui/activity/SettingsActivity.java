package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import de.psdev.licensesdialog.LicensesDialog;
import it.gmariotti.changelibs.library.view.ChangeLogListView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.DialogBuilder;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SearchRecentSuggestionsHelper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_ABOUT_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_CHANGELOG_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_CLEAR_FAVORITE_BUS_STOP_DATA;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_CLEAR_RECENT_SUGGESTIONS;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_OPEN_SOURCE_LICENSES_INFO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.PrefUtils.PREF_PRODUCT_TOUR;

/**
 * Preference screen for app preferences.
 *
 * @author Filipe Bezerra
 * @version 2.0, 10/03/2015
 * @since #
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
        toolbar.setTitle(getString(R.string.section_title_settings));
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

        switch (key) {
            case PREF_CLEAR_RECENT_SUGGESTIONS:
                new DialogBuilder(SettingsActivity.this, preference.getTitle().toString(),
                        getString(R.string.prompt_user_pref_clear_recent_suggestions))
                        .addAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SearchRecentSuggestionsHelper.getInstance(SettingsActivity.this)
                                        .clearHistory();
                                preference.setSummary(getString(R.string
                                        .pref_clear_recent_suggestions_summary_edited));
                                preference.setEnabled(false);
                            }
                        })
                        .addCancelButton()
                        .buildAndShow();
                return true;

            case PREF_CLEAR_FAVORITE_BUS_STOP_DATA:
                new DialogBuilder(SettingsActivity.this, preference.getTitle().toString(),
                        getString(R.string.prompt_user_pref_clear_favorite_bus_stop_data))
                        .addAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DaoManager.getInstance(SettingsActivity.this).getFavoriteBusStopDao()
                                        .deleteAll();
                                preference.setSummary(getString(R.string
                                        .pref_clear_favorite_bus_stop_data_summary_edited));
                                preference.setEnabled(false);

                                EventBusProvider.getInstance().getEventBus().post(
                                        new NotificationEvent(new NotificationMessage(
                                                NotificationMessage.NotificationType.RESET)));
                            }
                        })
                        .addCancelButton()
                        .buildAndShow();
                return false;

            case PREF_ABOUT_INFO:
                new DialogBuilder(SettingsActivity.this, getString(R.string.full_app_name),
                        getString(R.string.dialog_message_about_info))
                        .buildAndShow();
                return true;

            case PREF_OPEN_SOURCE_LICENSES_INFO:
                new LicensesDialog.Builder(this).setNotices(R.raw.notices)
                        .setThemeResourceId(R.style.Widget_LicensesDialog)
                        .setTitle(getString(R.string.pref_open_source_licenses_info_key_title))
                        .setDividerColorId(R.color.licenses_dialog_divider_color).build().show();
                return true;

            case PREF_CHANGELOG_INFO:
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                ChangeLogListView changeLogListView=(ChangeLogListView)layoutInflater.inflate(
                        R.layout.view_changelog, null);

                new AlertDialog.Builder(SettingsActivity.this).setView(changeLogListView)
                        .create().show();
                return true;

            case PREF_PRODUCT_TOUR:
                return false;
        }
        return false;
    }
}