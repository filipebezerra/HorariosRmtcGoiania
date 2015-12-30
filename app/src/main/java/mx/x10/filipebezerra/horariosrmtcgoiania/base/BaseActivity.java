package mx.x10.filipebezerra.horariosrmtcgoiania.base;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;

/**
 * Base activity for all activities in this application.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static final int NO_UP_INDICATOR = -1;
    @Nullable @Bind(R.id.toolbar) protected Toolbar mToolbarActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutResource());
        ButterKnife.bind(this);
        setupToolbarAsActionBar();
    }

    private void setupToolbarAsActionBar() {
        if (mToolbarActionBar != null) {
            setSupportActionBar(mToolbarActionBar);
            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);

                if (provideUpIndicator() != NO_UP_INDICATOR) {
                    final Drawable drawable = ContextCompat.getDrawable(this, provideUpIndicator());
                    DrawableHelper.tint(this, R.color.white, drawable);
                    actionBar.setHomeAsUpIndicator(drawable);
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    public int replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_placeholder, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        return transaction.commit();
    }

    public void changeTitleAndSubtitle(String newTitle, String subtitle) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(newTitle);
            actionBar.setSubtitle(subtitle);
        }
    }

    @LayoutRes protected abstract int provideLayoutResource();

    @NonNull public abstract ViewGroup getRootViewLayout();

    @DrawableRes protected int provideUpIndicator() {
        return View.NO_ID;
    }
}
