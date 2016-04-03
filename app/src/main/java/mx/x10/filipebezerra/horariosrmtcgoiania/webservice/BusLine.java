package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Class representation of a bus line.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class BusLine {
    @SerializedName("Numero")
    public String number;

    @SerializedName("Itinerario")
    public String itinerary;
}
