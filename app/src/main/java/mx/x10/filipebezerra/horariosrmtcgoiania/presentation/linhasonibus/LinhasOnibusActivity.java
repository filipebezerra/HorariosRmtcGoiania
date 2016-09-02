package mx.x10.filipebezerra.horariosrmtcgoiania.presentation.linhasonibus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.Injection;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.domain.pojo.LinhaOnibus;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.activity.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.util.FeedbackHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.presentation.widgets.DividerDecoration;

/**
 * @author Filipe Bezerra
 */
public class LinhasOnibusActivity extends BaseActivity implements LinhasOnibusContract.View,
        LinhasOnibusAdapter.LinhasOnibusAdapterCallback {

    public static final String EXTRA_NUMERO_PONTO
            = LinhasOnibusActivity.class.getSimpleName()+".extraLinhasOnibus";

    @BindView(R.id.linhas_onibus_container) protected CoordinatorLayout mLinhasOnibusContainerLayout;
    @BindView(R.id.recycler_view_linhas_onibus) protected RecyclerView mRecyclerViewLinhasOnibus;

    private LinhasOnibusContract.Presenter mPresenter;

    @Override
    protected int provideViewResource() {
        return R.layout.activity_linhas_onibus;
    }

    @Override
    protected void onCreate(@Nullable Bundle inState) {
        super.onCreate(inState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerViewLinhasOnibus.setHasFixedSize(true);
        mRecyclerViewLinhasOnibus.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewLinhasOnibus.addItemDecoration(new DividerDecoration(this));

        String numeroPonto = getIntent().getExtras().getString(EXTRA_NUMERO_PONTO);
        mPresenter = new LinhasOnibusPresenter(this, numeroPonto,
                Injection.providePrevisaoChegadaService(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.loadLinhasOnibus();
    }

    @Override
    public void showLinhasOnibus(List<LinhaOnibus> linhasOnibus) {
        LinhasOnibusAdapter adapter = new LinhasOnibusAdapter(this, linhasOnibus, this);
        mRecyclerViewLinhasOnibus.setAdapter(adapter);
    }

    @Override
    public void changeTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void changeSubtitle(CharSequence subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void showFeedbackMessage(CharSequence mensagem) {
        FeedbackHelper.snackbar(mLinhasOnibusContainerLayout, mensagem);
    }

    @Override
    public void onChevronClick(View view, LinhaOnibus linhaOnibus) {

    }
}
