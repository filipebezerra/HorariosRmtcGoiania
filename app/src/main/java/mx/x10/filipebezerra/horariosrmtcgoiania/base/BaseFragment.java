package mx.x10.filipebezerra.horariosrmtcgoiania.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.dialog.MaterialDialogHelper;
import timber.log.Timber;

/**
 * Base fragment.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public abstract class BaseFragment extends Fragment {
    protected MaterialDialogHelper mMaterialDialogHelper;

    public BaseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(provideLayoutResource(), container, false);
        ButterKnife.bind(this, view);
        return view;
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
