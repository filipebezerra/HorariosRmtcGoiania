package mx.x10.filipebezerra.horariosrmtcgoiania.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

public class FavoriteBusStopsAdapter
        extends RecyclerView.Adapter<FavoriteBusStopsAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<FavoriteBusStop> mFavoriteBusStopList;
    private OnItemClickListener mOnItemClickListener;

    public FavoriteBusStopsAdapter() {
    }

    public FavoriteBusStopsAdapter(@NonNull List<FavoriteBusStop> favoriteBusStopList) {
        mFavoriteBusStopList = favoriteBusStopList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item,
                parent, false);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FavoriteBusStop currentFavoriteBusStop = mFavoriteBusStopList.get(position);
        holder.mTextViewAddress.setText(currentFavoriteBusStop.getAddress());
        holder.mTextViewStopCode.setText(String.valueOf(currentFavoriteBusStop.getStopCode()));
        holder.itemView.setTag(currentFavoriteBusStop);
    }

    @Override
    public int getItemCount() {
        return mFavoriteBusStopList == null ? 0 : mFavoriteBusStopList.size();
    }

    public void addItem(FavoriteBusStop item) {
        if (mFavoriteBusStopList == null) {
            mFavoriteBusStopList = new ArrayList<>();
        }

        final int position = mFavoriteBusStopList.size() == 0 ? 0 : mFavoriteBusStopList.size() - 1;
        mFavoriteBusStopList.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(FavoriteBusStop item) {
        if (mFavoriteBusStopList == null) {
            return;
        }

        int index = mFavoriteBusStopList.indexOf(item);

        if (index != -1) {
            mFavoriteBusStopList.remove(item);
            notifyItemRemoved(index);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(final View view) {
        if (mOnItemClickListener != null) {
            FavoriteBusStop selectedFavoriteBusStop = (FavoriteBusStop) view.getTag();
            mOnItemClickListener.onItemClick(selectedFavoriteBusStop);
        }
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.favorite_item_stop_code) TextView mTextViewStopCode;

        @InjectView(R.id.favorite_item_address) TextView mTextViewAddress;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FavoriteBusStop favoriteBusStop);
    }
}
