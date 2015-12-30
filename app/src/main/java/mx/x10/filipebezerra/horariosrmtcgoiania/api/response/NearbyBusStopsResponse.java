package mx.x10.filipebezerra.horariosrmtcgoiania.api.response;

import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;


/**
 * Nearby bus stop API response.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 30/11/2015
 * @since 0.3.0
 */
public class NearbyBusStopsResponse extends BaseApiResponse {

    public List<BusStopModel> data;

}
