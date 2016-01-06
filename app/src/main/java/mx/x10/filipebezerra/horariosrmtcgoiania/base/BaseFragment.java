package mx.x10.filipebezerra.horariosrmtcgoiania.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.LayoutParams.ScrollFlags;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.dialog.MaterialDialogHelper;
import timber.log.Timber;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;

/**
 * Base fragment.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public abstract class BaseFragment extends Fragment {
    protected MaterialDialogHelper mMaterialDialogHelper;

    protected BaseActivity mBaseHostActivity;

    public BaseFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (mBaseHostActivity == null) {
            try {
                mBaseHostActivity = (BaseActivity) activity;
            } catch (ClassCastException e) {
                Timber.e("Descendants of %s must be hosted in instances of %s",
                        BaseFragment.class.getSimpleName(), BaseActivity.class.getSimpleName());
                throw e;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (mBaseHostActivity == null) {
            try {
                mBaseHostActivity = (BaseActivity) context;
            } catch (ClassCastException e) {
                Timber.e("Descendants of %s must be hosted in instances of %s",
                        BaseFragment.class.getSimpleName(), BaseActivity.class.getSimpleName());
                throw e;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(provideLayoutResource(), container, false);
        ButterKnife.bind(this, view);



        return view;
    }

    protected static final int TOOLBAR_SCROLL_DISABLED = 0x0;
    protected static final int TOOLBAR_SCROLL_QUICK_RETURN = SCROLL_FLAG_SCROLL
            | SCROLL_FLAG_ENTER_ALWAYS;

    protected void adaptActivityView(@NonNull String title, @Nullable String subtitle,
            @ScrollFlags int scrollStrategy) {
        mBaseHostActivity.changeTitleAndSubtitle(title, subtitle);

        final Toolbar actionBar = mBaseHostActivity.getToolbarActionBar();
        if (actionBar != null) {
            final ViewGroup.LayoutParams layoutParams = ((CardView)actionBar.getParent())
                    .getLayoutParams();

            if (layoutParams instanceof AppBarLayout.LayoutParams) {
                final AppBarLayout.LayoutParams appBarLayoutParams =
                        (AppBarLayout.LayoutParams) layoutParams;

                appBarLayoutParams.setScrollFlags(scrollStrategy);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMaterialDialogHelper = MaterialDialogHelper.toContext(getContext());
    }

    @Override
    public void onStop() {
        Timber.d("%s onStop()", getClass().getSimpleName());
        if (mMaterialDialogHelper != null) {
            mMaterialDialogHelper.dismissDialog();

            mMaterialDialogHelper = null;
        }

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Timber.d("%s onDestroyView()", getClass().getSimpleName());
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @LayoutRes protected abstract int provideLayoutResource();
}
