package mx.x10.filipebezerra.horariosrmtcgoiania.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Bus line API model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public class BusLineModel {
    @SerializedName("Numero")
    public String number;

    @SerializedName("Itinerario")
    public String itinerary;
}
