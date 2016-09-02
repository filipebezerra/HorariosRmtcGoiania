package mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Filipe Bezerra
 */
public class LinhaOnibus implements Parcelable {
    @SerializedName("Linha")
    @Expose
    public String numero;

    @SerializedName("Destino")
    @Expose
    public String destino;

    @SerializedName("Proximo")
    @Expose
    public PrevisaoChegada proximo;

    @SerializedName("Seguinte")
    @Expose
    public PrevisaoChegada seguinte;

    public static final Creator<LinhaOnibus> CREATOR = new Creator<LinhaOnibus>() {
        @Override
        public LinhaOnibus createFromParcel(Parcel in) {
            return new LinhaOnibus(in);
        }

        @Override
        public LinhaOnibus[] newArray(int size) {
            return new LinhaOnibus[size];
        }
    };

    protected LinhaOnibus(Parcel in) {
        numero = in.readString();
        destino = in.readString();
        proximo = in.readParcelable(PrevisaoChegada.class.getClassLoader());
        seguinte = in.readParcelable(PrevisaoChegada.class.getClassLoader());
    }

    public String getNumero() {
        return numero;
    }

    public String getDestino() {
        return destino;
    }

    public PrevisaoChegada getProximo() {
        return proximo;
    }

    public PrevisaoChegada getSeguinte() {
        return seguinte;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(numero);
        out.writeString(destino);
        out.writeParcelable(proximo, flags);
        out.writeParcelable(seguinte, flags);
    }
}
