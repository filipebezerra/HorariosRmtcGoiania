package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerCategory;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class DrawerAdapter extends BaseAdapter {

    public interface MenuListener {

        void onActiveViewChanged(View view);

    }

    private Context mContext;
    private List<DrawerItem> mItems;

    private MenuListener mListener;

    private int mActivePosition = -1;

    public DrawerAdapter(final Context context, List<DrawerItem> items) {
        mContext = context;
        mItems = items;
    }

    public void setListener(MenuListener mListener) {
        this.mListener = mListener;
    }

    public void setActivePosition(int activePosition) {
        this.mActivePosition = mActivePosition;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof DrawerItem ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof DrawerItem;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Object item = getItem(position);

        if (item instanceof DrawerCategory) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.menu_row_category, parent,
                        false);
            }

            ((TextView) view).setText(((DrawerCategory) item).getTitle());
        } else {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.menu_row_item, parent, false);
            }

            ImageView imageIcon = (ImageView) view.findViewById(R.id.menu_icon);
            TextView textTitle = (TextView) view.findViewById(R.id.menu_title);
            TextView textCounter = (TextView) view.findViewById(R.id.menu_counter);

            textTitle.setText(((DrawerItem) item).getTitle());
            imageIcon.setImageResource(((DrawerItem) item).getIconRes());

            if ((mItems.get(position)).isCounterVisible()) {
                textCounter.setText((mItems.get(position)).getCount());
            } else {
                textCounter.setVisibility(View.GONE);
            }
        }

        //view.setTag(R.id.mdActiveViewPosition, position);

        if (position == mActivePosition) {
            mListener.onActiveViewChanged(view);
        }

        return view;
    }

}
