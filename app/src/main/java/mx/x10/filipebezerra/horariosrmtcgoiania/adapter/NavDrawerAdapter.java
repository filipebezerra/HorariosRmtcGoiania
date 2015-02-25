package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuSection;

/**
 * Navigation drawer adapter specialized for the static menu items.
 *
 * @author Filipe Bezerra
 * @since 2.0
 */
public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem> implements Serializable {

    private LayoutInflater mInflater;

    public NavDrawerAdapter(Context context, int textViewResourceId, List<NavDrawerItem> objects ) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
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
        View view = convertView;
        NavMenuItemHolder holder;
        NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;

        if (view == null) {
            view = mInflater.inflate( R.layout.navdrawer_left_items, parentView, false);
            holder = new NavMenuItemHolder(view);
            view.setTag(holder);
        } else {
            holder = (NavMenuItemHolder) view.getTag();
        }

        holder.labelView.setText(menuItem.getLabel());

        if (holder.iconView != null) {
            holder.iconView.setImageResource(menuItem.getIcon());
        }

        if (holder.counterView != null) {
            if (menuItem.isCounterVisible()) {
                final BadgeView badgeView = new BadgeView(getContext(), holder.counterView);
                badgeView.setText(menuItem.getCount());
                badgeView.setBadgePosition(BadgeView.POSITION_CENTER);
                badgeView.setBadgeBackgroundColor(getContext().getResources()
                        .getColor(R.color.badge_counter_background));
                badgeView.show();
            } else {
                holder.counterView.setVisibility(View.GONE);
            }
        }

        return view ;
    }

    public View getSectionView(View convertView, ViewGroup parentView,
                               NavDrawerItem navDrawerItem) {
        View view = convertView;
        NavMenuSectionHolder holder;
        NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;

        if (view == null) {
            view = mInflater.inflate(R.layout.navdrawer_section, parentView, false);
            holder = new NavMenuSectionHolder(view);

            view.setTag(holder);
        } else {
            holder = (NavMenuSectionHolder) view.getTag();
        }

        holder.labelView.setText(menuSection.getLabel());

        return view;
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

    public List<NavDrawerItem> getAll() {
        final List<NavDrawerItem> items = new ArrayList<>();

        for(int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }

    public void replace(NavDrawerItem item) {
        int position = getPosition(item);
        remove(item);
        insert(item, position);
    }

    class NavMenuItemHolder {
        @InjectView(R.id.navdrawer_item_label)
        public TextView labelView;

        @InjectView(R.id.navdrawer_item_icon)
        public ImageView iconView;

        @InjectView(R.id.navdrawer_item_counter)
        public TextView counterView;

        public NavMenuItemHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    class NavMenuSectionHolder {
        @InjectView(R.id.navdrawer_section_label)
        public TextView labelView;

        public NavMenuSectionHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
