package mx.x10.filipebezerra.horariosrmtcgoiania.data.previsaochegada;

import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.ResponsePrevisaoChegada;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Filipe Bezerra
 */
public interface PrevisaoChegadaService {
    @POST("pontoparada/previsaochegada")
    Observable<ResponsePrevisaoChegada> previsaochegada(
            @Query("qryIdPontoParada") String idPontoParada);
}
