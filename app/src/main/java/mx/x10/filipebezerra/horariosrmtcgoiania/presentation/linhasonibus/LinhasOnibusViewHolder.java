package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;

/**
 * @author Filipe Bezerra
 */
class LinhasOnibusViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_view_numero_linha) TextView textViewNumeroLinha;
    @BindView(R.id.text_view_previsao_chegada) TextView textViewPrevisaoChegada;
    @BindView(R.id.text_view_destino) TextView textViewDestino;
    @BindView(R.id.chevron) ImageView chevron;

    LinhasOnibusViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
