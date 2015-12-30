package mx.x10.filipebezerra.horariosrmtcgoiania.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Arrival prediction API model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public class ArrivalPredictionModel {
    @SerializedName("Linha")
    public String lineNumber;

    @SerializedName("Destino")
    public String destination;

    @SerializedName("Proximo")
    public BusTravelModel next;

    @SerializedName("Seguinte")
    public BusTravelModel following;
}
