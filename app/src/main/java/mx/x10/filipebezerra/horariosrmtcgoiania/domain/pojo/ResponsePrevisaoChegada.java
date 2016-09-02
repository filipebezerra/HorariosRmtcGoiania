package mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filipe Bezerra
 */
public class ResponsePrevisaoChegada {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("mensagem")
    @Expose
    public String mensagem;

    @SerializedName("data")
    @Expose
    public List<LinhaOnibus> linhasOnibus = new ArrayList<LinhaOnibus>();

    public String getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<LinhaOnibus> getLinhasOnibus() {
        return linhasOnibus;
    }
}
