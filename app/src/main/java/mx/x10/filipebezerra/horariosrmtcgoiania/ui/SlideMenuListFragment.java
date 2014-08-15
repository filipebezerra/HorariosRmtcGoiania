package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.SlideMenuAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.SlideMenuItem;

/**
 * Fragmento do slide menu lateral. Esta classe constrói todo layout com a lista, itens da lista,
 * e o layout de cada item da lista.
 *
 * @author Filipe Bezerra
 * @since 1.3
 */
public class SlideMenuListFragment extends SherlockListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slide_menu_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SlideMenuAdapter adapter = new SlideMenuAdapter(getActivity());

        String[] titleServicosRmtc = getResources().getStringArray(R.array.servicos_rmtc);
        String[] descriptionServicosRmtc = getResources().getStringArray(R.array.servicos_rmtc_descricao);
        TypedArray imagesServicosRmtc = getResources().obtainTypedArray(R.array.servicos_rmtc_imagens);

        imagesServicosRmtc.recycle();

        for (int i = 0; i < titleServicosRmtc.length; i++) {
			adapter.add(new SlideMenuItem(titleServicosRmtc[i], descriptionServicosRmtc[i],
                    imagesServicosRmtc.getResourceId(i, -1)));
		}

		setListAdapter(adapter);
	}

}
