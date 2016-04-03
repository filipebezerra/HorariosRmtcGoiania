package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Class representation of a Web service call response.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 * @see BusStationService#lines(int)
 */
public class LinesResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("mensagem")
    public String message;

    @SerializedName("data")
    public BusStation busStation;
}
