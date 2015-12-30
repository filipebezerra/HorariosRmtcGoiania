package mx.x10.filipebezerra.horariosrmtcgoiania.api.service;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ArrivalPredictionResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.BusStopLinesResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.NearbyBusStopsResponse;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Bus stop API service.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public interface BusStopService {
    String BASE_URL = "http://novoapp.rmtcgoiania.com.br/";

    @POST("pontoparada/linhas")
    Observable<BusStopLinesResponse> searchBusStopLines(
            @Query("qryIdPontoParada") String busStopId);

    @POST("pontoparada/previsaochegada")
    Observable<ArrivalPredictionResponse> searchArrivalPrediction(
            @Query("qryIdPontoParada") String busStopId,
            @Query("qryLinha") String lineNumber);

    @POST("pontoparada/proximos")
    Observable<NearbyBusStopsResponse> searchNearbyBusStops(
            @Query("qryLatitude") double latitude,
            @Query("qryLongitude") double longitude,
            @Query("qryRaioDistancia") float radius);
}
