package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util;

import android.content.Intent;

/**
 * @author Filipe Bezerra
 */
public interface IntentHandler {
    void handleSearchIntent(Intent intent);

    interface IntentResultReceiver<T> {
        void onIntentResult(T result);

        void onResultNotHandled(T result);
    }
}
