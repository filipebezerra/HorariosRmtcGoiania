package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus;

import android.support.annotation.NonNull;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.data.previsaochegada.PrevisaoChegadaService;
import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.LinhaOnibus;
import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.ResponsePrevisaoChegada;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Filipe Bezerra
 */
public class LinhasOnibusPresenter implements LinhasOnibusContract.Presenter {
    private final LinhasOnibusContract.View mView;

    private final String mNumeroPonto;

    private final PrevisaoChegadaService mPrevisaoChegadaService;

    private List<LinhaOnibus> mLinhasOnibus;

    public LinhasOnibusPresenter(
            @NonNull LinhasOnibusContract.View view,
            @NonNull String numeroPonto,
            @NonNull PrevisaoChegadaService previsaoChegadaService) {
        mView = view;
        mNumeroPonto = numeroPonto;
        mPrevisaoChegadaService = previsaoChegadaService;
        mView.changeTitle(numeroPonto);
    }

    @Override
    public void loadLinhasOnibus() {
        mPrevisaoChegadaService
                .previsaochegada(mNumeroPonto)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<ResponsePrevisaoChegada>() {
                            @Override
                            public void call(ResponsePrevisaoChegada response) {
                                if (Boolean.parseBoolean(response.status)) {
                                    mView.showLinhasOnibus(response.getLinhasOnibus());
                                } else {
                                    mView.showFeedbackMessage(response.getMensagem());
                                }
                            }
                        }
                );
    }
}
