package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.main;

import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.IntentHandler.IntentResultReceiver;

/**
 * @author Filipe Bezerra
 */
public interface MainContract {
    interface View {
        void navigateToLinhasOnibusScreen(String numeroPonto);

        void showSearchResult(String result);
    }

    interface Presenter extends IntentResultReceiver<String> {

    }
}
