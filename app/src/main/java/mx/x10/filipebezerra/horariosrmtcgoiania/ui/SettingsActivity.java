package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * Tela de configuração das preferências do usuário.<br />
 * Por esta tela e possível configurar todas preferências aplicadas ao aplicativo e que
 * coordenam as funcionalidades disponíveis.
 *
 * @author Filipe Bezerra
 * @since 1.0
 */
public class SettingsActivity extends SherlockPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return false;
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
