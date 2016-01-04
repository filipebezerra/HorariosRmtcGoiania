package mx.x10.filipebezerra.horariosrmtcgoiania.base;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import mx.x10.filipebezerra.horariosrmtcgoiania.dialog.MaterialDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;
import timber.log.Timber;

/**
 * Base activity for all activities in this application.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final String STATE_FRAGMENT_IN_CONTAINER = "State_FragmentInContainer";

    protected static final int NO_UP_INDICATOR = -1;

    protected MaterialDialogHelper mMaterialDialogHelper;

    @Nullable @Bind(R.id.toolbar) protected Toolbar mToolbarActionBar;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(provideLayoutResource());
        ButterKnife.bind(this);
        setupToolbarAsActionBar();
        mMaterialDialogHelper = MaterialDialogHelper.toContext(this);
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

    @Override
    protected void onStop() {
        super.onStop();

        if (mMaterialDialogHelper != null) {
            mMaterialDialogHelper.dismissDialog();
        }
    }

    /**
     * Adds, replaces or reattaches the given {@code fragment} to the fragment container.
     *
     * @param fragment the fragment.
     * @param addToBackStack {@code true} if has to be added in the back stack.
     * @return true is {@code fragment} is existing in that container.
     */
    @SuppressLint("CommitTransaction")
    public int replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        Timber.d("Caller want to add the fragment %s.", fragment.getClass().getSimpleName());

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        // fragment tag
        final String tagName = fragment.getClass().getSimpleName();

        final Fragment fragmentFound = fragmentManager
                .findFragmentById(R.id.fragment_placeholder);

        // no fragments in the fragment container, so ADD
        if (fragmentFound == null) {
            Timber.d("Fragment container is empty. Adding the fragment to it using tag %s",
                    tagName);

            fragmentTransaction
                    .add(R.id.fragment_placeholder, fragment, tagName);
        }

        // contains a fragment in container but isn't the same, so REPLACE
        else {
            Timber.d("Fragment container has another fragment. "
                    + "Replacing with this fragment using tag %s", tagName);

            fragmentTransaction
                    .replace(
                            R.id.fragment_placeholder, fragment, tagName);
        }

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tagName);
        }

        final int id = fragmentTransaction.commit();
        Timber.d("Transaction commit with id %d", id);

        return id;
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