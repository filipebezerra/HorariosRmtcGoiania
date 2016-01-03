package mx.x10.filipebezerra.horariosrmtcgoiania.application;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.parse.ParseSubclasses;
import timber.log.Timber;

/**
 * Application class.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 31/12/2015
 * @since 0.3.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        enableLogging();
        enableParseIntegration();
    }

    private void enableLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void enableParseIntegration() {
        // Register custom Parse subclasses
        ParseSubclasses.registerSubclasses();

        // Enable local datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, getString(R.string.parse_application_id),
                getString(R.string.parse_client_key));

        // Save the current installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
