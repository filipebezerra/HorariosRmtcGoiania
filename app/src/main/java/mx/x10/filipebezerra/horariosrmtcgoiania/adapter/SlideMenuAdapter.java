package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.SlideMenuItem;

/**
 * Classe adaptadora dos itens do slide menu, os dados carregados do array e seu layout personalizado.
 *
 * @author Filipe Bezerra
 * @since 1.3
 */
public class SlideMenuAdapter extends ArrayAdapter<SlideMenuItem> {

    public SlideMenuAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.slide_menu_list_row, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.icon.setImageResource(getItem(position).iconRes);
        viewHolder.title.setText(getItem(position).title);
        viewHolder.description.setText(getItem(position).description);

        return view;
    }

    static class ViewHolder {

        @InjectView(R.id.row_icon)
        ImageView icon;

        @InjectView(R.id.row_title)
        TextView title;

        @InjectView(R.id.row_description)
        TextView description;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


}
