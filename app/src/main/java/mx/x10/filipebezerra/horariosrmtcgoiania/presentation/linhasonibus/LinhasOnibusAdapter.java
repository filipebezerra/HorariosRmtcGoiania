package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.LinhaOnibus;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.FormattingUtil;

/**
 * @author Filipe Bezerra
 */
class LinhasOnibusAdapter extends RecyclerView.Adapter<LinhasOnibusViewHolder> {
    private final Context mContext;

    private final List<LinhaOnibus> mLinhasOnibus;

    private final LinhasOnibusAdapterCallback mCallback;

    LinhasOnibusAdapter(
            @NonNull Context context,
            @NonNull List<LinhaOnibus> linhasOnibus,
            @NonNull LinhasOnibusAdapterCallback callback) {
        mContext = context;
        mLinhasOnibus = linhasOnibus;
        mCallback = callback;
    }

    @Override
    public LinhasOnibusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_linha_onibus, parent, false);
        return new LinhasOnibusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LinhasOnibusViewHolder holder, int position) {
        LinhaOnibus linhaOnibus = mLinhasOnibus.get(position);

        holder.textViewDestino.setText(
                mContext.getString(
                        R.string.template_destino_linha_onibus, linhaOnibus.getDestino()));

        holder.textViewPrevisaoChegada.setText(
                FormattingUtil.formatAsRelativeDateFromNow(
                        linhaOnibus.getProximo().getHoraChegadaPrevista()));

        holder.textViewNumeroLinha.setText(linhaOnibus.getNumero());

        holder.chevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onChevronClick(
                        holder.itemView, mLinhasOnibus.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLinhasOnibus.size();
    }

    interface LinhasOnibusAdapterCallback {
        void onChevronClick(View view, LinhaOnibus linhaOnibus);
    }
}
