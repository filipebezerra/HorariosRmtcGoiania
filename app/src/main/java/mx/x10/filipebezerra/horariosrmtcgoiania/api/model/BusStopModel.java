package mx.x10.filipebezerra.horariosrmtcgoiania.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Bus stop API model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public class BusStopModel {
    @SerializedName("IdPontoParada")
    public String id;

    @SerializedName("Endereco")
    public String address;

    @SerializedName("Latitude")
    public String latitude;

    @SerializedName("Longitude")
    public String longitude;

    @SerializedName("Linhas")
    public List<BusLineModel> lines;
}
