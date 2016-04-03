package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public interface BusStationService {
    @POST("pontoparada/linhas")
    Observable<LinesResponse> lines(@Query("qryIdPontoParada") int busStationId);
}
