package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.widgets.Dialog;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SearchRecentSuggestionsHelper;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 27/02/2015
 * @since #
 */
public class SettingsActivity extends PreferenceActivity 
        implements View.OnClickListener, Preference.OnPreferenceClickListener {

    private static final String PREF_CLEAR_RECENT_SUGGESTIONS_KEY = "clear_recent_suggestions";
    private static final String PREF_CLEAR_FAVORITE_BUS_STOP_DATA_KEY = "clear_favorite_bus_stop_data";
    private static final String PREF_ABOUT_INFO_KEY = "about_info";
    private static final String PREF_OPEN_SOURCE_LICENSES_INFO_KEY = "open_source_licenses_info";
    private static final String PREF_CHANGELOG_INFO_KEY = "changelog_info";
    private static final String PREF_PRODUCT_TOUR_KEY = "product_tour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();
        addPreferencesFromResource(R.xml.settings);
        findPreference(PREF_CLEAR_RECENT_SUGGESTIONS_KEY).setOnPreferenceClickListener(this);
        findPreference(PREF_ABOUT_INFO_KEY).setOnPreferenceClickListener(this);
    }

    private void setupContentView() {
        setContentView(R.layout.activity_settings);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
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
        Dialog dialog;
        
        switch (key) {
            case PREF_CLEAR_RECENT_SUGGESTIONS_KEY:
                dialog = new Dialog(SettingsActivity.this, preference.getTitle().toString(),
                        "Todas sugestões de pesquisa serão removidas.");
                dialog.addCancelButton("CANCELAR");

                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SearchRecentSuggestionsHelper.getInstance(SettingsActivity.this)
                                .clearHistory();
                        preference.setSummary("As sugestões foram removidas.");
                        preference.setEnabled(false);
                    }
                });
                dialog.show();
                dialog.getButtonAccept().setText("OK");
                return true;
            
            case PREF_CLEAR_FAVORITE_BUS_STOP_DATA_KEY:
                return false;
            
            case PREF_ABOUT_INFO_KEY:
                dialog = new Dialog(SettingsActivity.this, getString(R.string.full_app_name),
                        getString(R.string.dialog_message_about_info));
                dialog.show();
                dialog.getButtonAccept().setText("OK");
                return true;
            
            case PREF_OPEN_SOURCE_LICENSES_INFO_KEY:
                return false;
            
            case PREF_CHANGELOG_INFO_KEY:
                return false;
            
            case PREF_PRODUCT_TOUR_KEY:
                return false;
        }
        return false;
    }
    
}
