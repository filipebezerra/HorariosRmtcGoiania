package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.slide_menu_list_row, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
        icon.setImageResource(getItem(position).iconRes);

        TextView title = (TextView) convertView.findViewById(R.id.row_title);
        title.setText(getItem(position).title);

        TextView description = (TextView) convertView.findViewById(R.id.row_description);
        description.setText(getItem(position).description);

        return convertView;
    }

}
