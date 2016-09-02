package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.main;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.IntentHandler;

/**
 * @author Filipe Bezerra
 */
class SearchIntentHandler implements IntentHandler {
    private static final String ACTION_VOICE_SEARCH
            = "com.google.android.gms.actions.SEARCH_ACTION";

    private final IntentResultReceiver<String> mIntentResultReceiver;

    SearchIntentHandler(@NonNull IntentResultReceiver<String> intentResultReceiver) {
        mIntentResultReceiver = intentResultReceiver;
    }

    @Override
    public void handleSearchIntent(Intent intent) {
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())
                    || ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                if (ACTION_VOICE_SEARCH.equals(intent.getAction())) {
                    mIntentResultReceiver.onResultNotHandled(query);
                } else {
                    if (!TextUtils.isEmpty(query)) {
                        mIntentResultReceiver.onIntentResult(query);
                    }
                }
            }
        }
    }
}
