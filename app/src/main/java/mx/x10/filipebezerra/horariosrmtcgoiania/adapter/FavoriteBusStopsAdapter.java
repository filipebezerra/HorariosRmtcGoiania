package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

public class FavoriteBusStopsAdapter extends BaseAdapter {
    private LayoutInflater mLayout;

    private List<FavoriteBusStop> mData;

    public FavoriteBusStopsAdapter(Context context,
                                   List<FavoriteBusStop> pontosParada) {
        mLayout = LayoutInflater.from(context);
        mData = pontosParada;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData != null ? mData.get(position) :
                null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(FavoriteBusStop ponto) {
        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.add(ponto);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        FavoriteBusStop ponto = (FavoriteBusStop) getItem(position);

        if (v == null) {
            v = mLayout.inflate(R.layout.navdrawer_right_items, parent, false);
            holder = new ViewHolder();

            holder.mNumeroPontoTextView = (TextView) v.findViewById(R.id.numeroPontoItem);
            holder.mReferenciaTextView = (TextView) v.findViewById(R.id.referenciaPontoItem);
            holder.mEnderecoTextView = (TextView) v.findViewById(R.id.enderecoPontoItem);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.mNumeroPontoTextView.setText(String.valueOf(ponto.getStopCode()));
        holder.mReferenciaTextView.setText(ponto.getStopReference());
        holder.mEnderecoTextView.setText(ponto.getAddress());

        return v;
    }

    private class ViewHolder {
        public TextView mNumeroPontoTextView;
        public TextView mReferenciaTextView;
        public TextView mEnderecoTextView;
    }

}
