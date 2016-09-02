package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.main;

import android.support.annotation.NonNull;

/**
 * @author Filipe Bezerra
 */
public class MainPresenter implements MainContract.Presenter {
    private final MainContract.View mView;

    public MainPresenter(@NonNull MainContract.View view) {
        mView = view;
    }

    @Override
    public void onIntentResult(String result) {
        mView.navigateToLinhasOnibusScreen(result);
    }

    @Override
    public void onResultNotHandled(String result) {
        mView.showSearchResult(result);
    }
}
