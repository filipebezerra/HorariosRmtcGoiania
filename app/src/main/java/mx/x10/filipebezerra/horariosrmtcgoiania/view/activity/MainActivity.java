package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
