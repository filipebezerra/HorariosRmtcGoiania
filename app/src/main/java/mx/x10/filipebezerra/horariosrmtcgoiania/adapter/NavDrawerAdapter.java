package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavMenuSection;

/**
 * @author Filipe Bezerra,
 *          Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> {

    private LayoutInflater inflater;

    public NavDrawerAdapter(Context context, int textViewResourceId, NavDrawerItem[] objects ) {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null ;

        NavDrawerItem menuItem = this.getItem(position);

        if ( menuItem.getType() == NavMenuItem.ITEM_TYPE ) {
            view = getItemView(convertView, parent, menuItem );
        } else {
            view = getSectionView(convertView, parent, menuItem);
        }
        return view ;
    }

    public View getItemView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {

        NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
        NavMenuItemHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.navdrawer_item, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById(R.id.navdrawer_item_label);
            ImageView iconView = (ImageView) convertView
                    .findViewById(R.id.navdrawer_item_icon);
            TextView counterView = (TextView) convertView.findViewById(R.id.navdrawer_item_counter);

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.labelView = labelView ;
            navMenuItemHolder.iconView = iconView ;
            navMenuItemHolder.counterView = counterView;

            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(menuItem.getLabel());

        if (navMenuItemHolder.iconView != null) {
            navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
        }

        if (navMenuItemHolder.counterView != null) {
            if (menuItem.isCounterVisible()) {
                navMenuItemHolder.counterView.setText(menuItem.getCount());
            } else {
                navMenuItemHolder.counterView.setVisibility(View.GONE);
            }
        }

        return convertView ;
    }

    public View getSectionView(View convertView, ViewGroup parentView,
                               NavDrawerItem navDrawerItem) {

        NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;
        NavMenuSectionHolder navMenuItemHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate( R.layout.navdrawer_section, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById(R.id.navdrawer_section_label);

            navMenuItemHolder = new NavMenuSectionHolder();
            navMenuItemHolder.labelView = labelView ;
            convertView.setTag(navMenuItemHolder);
        }

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(menuSection.getLabel());

        return convertView ;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getType();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }


    private static class NavMenuItemHolder {
        private TextView labelView;
        private ImageView iconView;
        private TextView counterView;
    }

    private class NavMenuSectionHolder {
        private TextView labelView;
    }

}
