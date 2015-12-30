package mx.x10.filipebezerra.horariosrmtcgoiania.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Base API response model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public abstract class BaseApiResponse {
    public String status;

    @SerializedName("mensagem")
    public String message;
}
