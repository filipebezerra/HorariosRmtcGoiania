package mx.x10.filipebezerra.horariosrmtcgoiania.nearby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 30/11/2015
 * @since 0.3.0
 */
public class NearbyBusStopsAdapter
        extends RecyclerView.Adapter<NearbyBusStopsAdapter.NearBusStopsViewHolder> {

    @NonNull private final Context mContext;
    @NonNull private List<BusStop> mBusStops;

    private OnRequestAvailableLines mOnRequestAvailableLines;

    public NearbyBusStopsAdapter(@NonNull Context context, @NonNull List<BusStop> list) {
        mContext = context;
        mBusStops = list;
    }

    @Override
    public NearBusStopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.nearby_bus_stops, parent, false);
        return new NearBusStopsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NearBusStopsViewHolder holder, int position) {
        final BusStop busStop = mBusStops.get(position);
        holder.busStopId.setText(busStop.getId());
        holder.busStopAddress.setText(busStop.getAddress());
    }

    @Override
    public int getItemCount() {
        return mBusStops.size();
    }

    public void swapData(@NonNull final List<BusStop> busStops) {
        mBusStops = busStops;
        notifyDataSetChanged();
    }

    public void setOnRequestAvailableLines(OnRequestAvailableLines callback) {
        mOnRequestAvailableLines = callback;
    }

    class NearBusStopsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.bus_stop_id) TextView busStopId;
        @Bind(R.id.bus_stop_address) TextView busStopAddress;

        public NearBusStopsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.available_lines)
        public void availableLines() {
            if (mOnRequestAvailableLines != null) {
                final BusStop busStop = mBusStops.get(getAdapterPosition());
                mOnRequestAvailableLines.onRequest(busStop.getId(), busStop.getLines());
            }
        }
    }

    public interface OnRequestAvailableLines {
        void onRequest(String busStopId, List<BusLine> lines);
    }
}
