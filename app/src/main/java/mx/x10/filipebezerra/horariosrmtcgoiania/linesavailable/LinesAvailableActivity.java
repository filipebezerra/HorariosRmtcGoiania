package mx.x10.filipebezerra.horariosrmtcgoiania.linesavailable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.webservice.BusStation;

public class LinesAvailableActivity extends AppCompatActivity {
    public static final String EXTRA_BUS_STATION = "BusStation";

    private BusStation mBusStation;

    private LinesAvailableAdapter mLinesAvailableAdapter;

    @Bind(android.R.id.list) RecyclerView mLinesAvailableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines_available);
        ButterKnife.bind(this);

        mBusStation = getIntent().getParcelableExtra(EXTRA_BUS_STATION);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mBusStation.busStationId);
        getSupportActionBar().setSubtitle(mBusStation.address);

        mLinesAvailableView.setHasFixedSize(true);
        mLinesAvailableView.setLayoutManager(new LinearLayoutManager(this));
        mLinesAvailableView.setAdapter(
                mLinesAvailableAdapter = new LinesAvailableAdapter(this, mBusStation.lines));
    }
}
