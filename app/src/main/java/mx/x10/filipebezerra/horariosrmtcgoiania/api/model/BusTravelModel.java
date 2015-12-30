package mx.x10.filipebezerra.horariosrmtcgoiania.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Bus travel API model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public class BusTravelModel {
    @SerializedName("Qualidade")
    public String quality;

    @SerializedName("NumeroOnibus")
    public String busNumber;

    @SerializedName("HoraChegadaPlanejada")
    public String plannedArrivalTime;

    @SerializedName("HoraChegadaPrevista")
    public String expectedArrivalTime;

    @SerializedName("PrevisaoChegada")
    public int minutesToArrive;
}
