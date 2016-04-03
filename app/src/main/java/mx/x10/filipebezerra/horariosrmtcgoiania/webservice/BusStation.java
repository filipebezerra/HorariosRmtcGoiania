package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a bus station, or a bus stop.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class BusStation {
    @SerializedName("IdPontoParada")
    public String busStationId;

    @SerializedName("Endereco")
    public String address;

    @SerializedName("Latitude")
    public String latitude;

    @SerializedName("Longitude")
    public String longitude;

    @SerializedName("Linhas")
    public List<BusLine> lines = new ArrayList<>();
}
