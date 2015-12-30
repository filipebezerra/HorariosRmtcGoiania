package mx.x10.filipebezerra.horariosrmtcgoiania.busstop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 01/12/2015
 * @since 0.1.0
 */
public class BusStopLinesAdapter
        extends RecyclerView.Adapter<BusStopLinesAdapter.BusStopLinesViewHolder> {

    @NonNull private final Context mContext;
    @NonNull private BusStop mBusStop;

    private OnRequestArrivalPrediction mOnRequestArrivalPrediction;

    public BusStopLinesAdapter(@NonNull Context context, @NonNull BusStop busStop) {
        mContext = context;
        mBusStop = busStop;
    }

    @Override
    public BusStopLinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.bus_stop_lines, parent, false);
        return new BusStopLinesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BusStopLinesViewHolder holder, int position) {
        final BusLine busLine = mBusStop.getLines().get(position);
        holder.lineNumber.setText(busLine.getNumber());
        holder.lineItinerary.setText(busLine.getItinerary());
    }

    @Override
    public int getItemCount() {
        return mBusStop.getLines().size();
    }

    public void swapData(@NonNull final BusStop busStop) {
        mBusStop = busStop;
        notifyDataSetChanged();
    }

    public void setOnRequestArrivalPrediction(
            OnRequestArrivalPrediction onRequestArrivalPrediction) {
        mOnRequestArrivalPrediction = onRequestArrivalPrediction;
    }

    class BusStopLinesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.line_number) TextView lineNumber;
        @Bind(R.id.line_itinerary) TextView lineItinerary;

        public BusStopLinesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.next_travel)
        public void nextTravel() {
            if (mOnRequestArrivalPrediction != null) {
                mOnRequestArrivalPrediction.onRequest(mBusStop.getId(),
                        mBusStop.getLines().get(getAdapterPosition()).getNumber());
            }
        }
    }

    public interface OnRequestArrivalPrediction {
        void onRequest(String busStopId, String busLineNumber);
    }
}
