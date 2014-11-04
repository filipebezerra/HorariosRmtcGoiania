package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.SuperCardToast;

/**
 * @author Filipe Bezerra
 * @since 1.6
 */
public class HorarioViagemFragment extends BaseWebViewFragment {

    public HorarioViagemFragment() {
    }

    @SuppressLint("ValidFragment")
    public HorarioViagemFragment(final Bundle args) {
        super(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SuperCardToast.onRestoreState(savedInstanceState, getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
