package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

public class FavoriteBusStopsAdapter extends ArrayAdapter<FavoriteBusStop> {

    private int mLayoutResource;

    public FavoriteBusStopsAdapter(Context context, int resource, List<FavoriteBusStop> objects) {
        super(context, resource, objects);
        mLayoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        FavoriteBusStop ponto = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(mLayoutResource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mNumeroPontoTextView.setText(String.valueOf(ponto.getStopCode()));
        holder.mEnderecoTextView.setText(ponto.getAddress());

        return view;
    }

    class ViewHolder {
        @InjectView(R.id.numeroPontoItem)
        public TextView mNumeroPontoTextView;

        @InjectView(R.id.enderecoPontoItem)
        public TextView mEnderecoTextView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
