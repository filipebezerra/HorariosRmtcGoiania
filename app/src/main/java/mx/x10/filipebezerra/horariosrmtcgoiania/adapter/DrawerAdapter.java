package mx.x10.filipebezerra.horariosrmtcgoiania.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerHeader;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class DrawerAdapter extends BaseAdapter {

    public interface MenuListener {
        void onActiveViewChanged(View view);
    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<DrawerHeader> mData = new ArrayList<>();
    private TreeSet<Integer> mSectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    private MenuListener mListener;

    private int mActivePosition = -1;

    public DrawerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setListener(MenuListener mListener) {
        this.mListener = mListener;
    }

    public void setActivePosition(int activePosition) {
        this.mActivePosition = mActivePosition;
    }

    public void addItem(final DrawerItem item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final DrawerHeader header) {
        mData.add(header);
        mSectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DrawerHeader getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.drawer_menu_item, null);
                    holder.mTitle = (TextView) convertView.findViewById(R.id.menu_title);
                    holder.mIconRes = (ImageView) convertView.findViewById(R.id.menu_icon);
                    holder.mCounter = (TextView) convertView.findViewById(R.id.menu_counter);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.drawer_menu_header, null);
                    holder.mTitle = (TextView) convertView.findViewById(R.id.menu_header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(mData.get(position).getTitle());

        if (holder.mIconRes != null) {
            holder.mIconRes.setImageResource(((DrawerItem) mData.get(position)).getIconRes());
        }

        if (holder.mCounter != null) {
            if (((DrawerItem) mData.get(position)).isCounterVisible()) {
                holder.mCounter.setText(((DrawerItem) mData.get(position)).getCount());
            } else {
                holder.mCounter.setVisibility(View.GONE);
            }
        }

        if (position == mActivePosition) {
            mListener.onActiveViewChanged(convertView);
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView mTitle;
        public ImageView mIconRes;
        public TextView mCounter;
    }



/*
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
                view = LayoutInflater.from(mContext).inflate(R.layout.drawer_menu_header, parent,
                        false);
            }

            ((TextView) view).setText(((DrawerCategory) item).getTitle());
        } else {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.drawer_menu_item, parent, false);
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
*/
}
