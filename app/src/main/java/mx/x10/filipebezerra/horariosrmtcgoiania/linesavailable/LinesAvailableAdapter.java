package mx.x10.filipebezerra.horariosrmtcgoiania.linesavailable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.webservice.BusLine;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class LinesAvailableAdapter extends RecyclerView.Adapter<LinesAvailableAdapter.ViewHolder> {
    @NonNull private final Context mContext;
    @NonNull private List<BusLine> mBusLines;

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.arrival_forecast) TextView arrivalForecast;
        @Bind(R.id.line_number) TextView lineNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LinesAvailableAdapter(@NonNull Context context, @NonNull List<BusLine> busLines) {
        mContext = context;
        mBusLines = busLines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_lines_available, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BusLine busLine = mBusLines.get(position);
        holder.lineNumber.setText(busLine.number);
    }

    @Override
    public int getItemCount() {
        return mBusLines.size();
    }
}
