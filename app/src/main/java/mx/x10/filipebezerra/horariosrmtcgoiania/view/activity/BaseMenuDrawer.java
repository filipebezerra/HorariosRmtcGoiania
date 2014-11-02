package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.DrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public abstract class BaseMenuDrawer extends ActionBarActivity implements DrawerAdapter.MenuListener {

    private static final String STATE_ACTIVE_POSITION = BaseMenuDrawer.class.getName()
            .concat("ACTIVE_POSITION");

    protected MenuDrawer mMenuDrawer;

    protected DrawerAdapter mMenuAdapter;

    protected ListView mListView;

    private int mActivePosition = 0;

    protected List<Object> mItems = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);

        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY,
                getDrawerPosition(), getDragMode());


        mListView = new ListView(this);

        //mMenuAdapter = new DrawerAdapter(this, mItems);
        mMenuAdapter.setListener(this);
        mMenuAdapter.setActivePosition(mActivePosition);

        mListView.setAdapter(mMenuAdapter);
        mListView.setOnItemClickListener(mItemClickListener);

        mMenuDrawer.setMenuView(mListView);
    }

    protected abstract void onMenuItemClicked(int position, DrawerItem item);

    protected abstract int getDragMode();

    protected abstract Position getDrawerPosition();

    protected abstract List<Object> setItems(final List<Object> items);

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mMenuAdapter.setActivePosition(position);
            onMenuItemClicked(position, (DrawerItem) mMenuAdapter.getItem(position));
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

    @Override
    public void onActiveViewChanged(View v) {
        mMenuDrawer.setActiveView(v, mActivePosition);
    }

}