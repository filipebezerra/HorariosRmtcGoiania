package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus;

import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.LinhaOnibus;

/**
 * @author Filipe Bezerra
 */
public interface LinhasOnibusContract {
    interface View {
        void showLinhasOnibus(List<LinhaOnibus> linhasOnibus);

        void changeTitle(CharSequence title);

        void changeSubtitle(CharSequence subtitle);

        void showFeedbackMessage(CharSequence mensagem);
    }

    interface Presenter {
        void loadLinhasOnibus();
    }
}
