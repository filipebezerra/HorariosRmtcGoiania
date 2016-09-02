package mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Filipe Bezerra
 */
public class PrevisaoChegada implements Parcelable {
    @SerializedName("Qualidade")
    @Expose
    public String qualidade;

    @SerializedName("NumeroOnibus")
    @Expose
    public String numeroOnibus;

    @SerializedName("HoraChegadaPlanejada")
    @Expose
    public String horaChegadaPlanejada;

    @SerializedName("HoraChegadaPrevista")
    @Expose
    public String horaChegadaPrevista;

    @SerializedName("PrevisaoChegada")
    @Expose
    public Integer previsaoChegada;

    public static final Creator<PrevisaoChegada> CREATOR = new Creator<PrevisaoChegada>() {
        @Override
        public PrevisaoChegada createFromParcel(Parcel in) {
            return new PrevisaoChegada(in);
        }

        @Override
        public PrevisaoChegada[] newArray(int size) {
            return new PrevisaoChegada[size];
        }
    };

    protected PrevisaoChegada(Parcel in) {
        qualidade = in.readString();
        numeroOnibus = in.readString();
        horaChegadaPlanejada = in.readString();
        horaChegadaPrevista = in.readString();
        previsaoChegada = in.readInt();
    }

    public String getQualidade() {
        return qualidade;
    }

    public String getNumeroOnibus() {
        return numeroOnibus;
    }

    public String getHoraChegadaPlanejada() {
        return horaChegadaPlanejada;
    }

    public String getHoraChegadaPrevista() {
        return horaChegadaPrevista;
    }

    public Integer getPrevisaoChegada() {
        return previsaoChegada;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(qualidade);
        out.writeString(numeroOnibus);
        out.writeString(horaChegadaPlanejada);
        out.writeString(horaChegadaPrevista);
        out.writeInt(previsaoChegada);
    }
}
