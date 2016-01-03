package mx.x10.filipebezerra.horariosrmtcgoiania.busterminal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.parse.BaseBusStopParse;
import mx.x10.filipebezerra.horariosrmtcgoiania.parse.BusTerminalParse;
import timber.log.Timber;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.0
 */
public class BusTerminalAdapter extends RecyclerView.Adapter<BusTerminalAdapter.ViewHolder> {
    @NonNull private final Context mContext;

    private List<BusTerminalParse> mBusTerminals = new ArrayList<>();

    private final Gson mGson = new Gson();

    public BusTerminalAdapter(@NonNull Context context) {
        mContext = context;
        retrieveFromLocalStorage();
    }

    private void retrieveFromLocalStorage() {
        ParseQuery.getQuery(BusTerminalParse.class)
                .orderByAscending(BaseBusStopParse.KEY_ENDERECO)
                .fromPin(BusTerminalParse.LOCAL_NAME)
                .findInBackground((localBusTerminals, e) -> {
                    if (e == null) {
                        if (localBusTerminals == null || localBusTerminals.isEmpty()) {
                            Timber.d(
                                    "No bus terminals stored locally. Fetching from cloud storage.");
                            retrieveFromCloudStorage();
                        } else {
                            Timber.d("Found %d bus terminals stored locally.",
                                    localBusTerminals.size());
                            mBusTerminals = localBusTerminals;
                            notifyDataSetChanged();
                        }
                    } else {
                        Timber.e(e, "Querying bus terminals from local storage.");
                    }
                });
    }

    private void retrieveFromCloudStorage() {
        ParseQuery.getQuery(BusTerminalParse.class)
                .orderByAscending(BaseBusStopParse.KEY_ENDERECO)
                .findInBackground((busTerminals, findError) -> {
                    if (findError == null) {
                        if (busTerminals == null || busTerminals.isEmpty()) {
                            Timber.d("No bus terminals stored in cloud.");
                        } else {
                            Timber.d("Found %d bus terminals stored in cloud.",
                                    busTerminals.size());

                            mBusTerminals = busTerminals;
                            notifyDataSetChanged();

                            ParseObject.unpinAllInBackground(BusTerminalParse.LOCAL_NAME,
                                    unpinError -> {
                                        if (unpinError == null) {
                                            Timber.d("Old bus terminals was deleted "
                                                    + "from local storage.");
                                        } else {
                                            Timber.e(unpinError, "Unpinning bus terminals "
                                                    + "from local storage.");
                                        }
                                    });

                            ParseObject.pinAllInBackground(BusTerminalParse.LOCAL_NAME,
                                    busTerminals,
                                    pinError -> {
                                        if (pinError == null) {
                                            Timber.d("Bus terminals was saved in local "
                                                    + "storage.");
                                        } else {
                                            Timber.e(pinError, "Saving bus terminals in "
                                                    + "local storage.");
                                        }
                                    });
                        }
                    } else {
                        Timber.e(findError, "Querying bus terminals from cloud storage.");
                    }
                });
    }

    @Override
    public BusTerminalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.bus_terminal, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BusTerminalParse busTerminal = mBusTerminals.get(position);

        holder.busTerminalId.setText(busTerminal.getIdPontoParada());
        holder.busTerminalAddress.setText(busTerminal.getEndereco());
    }

    @Override
    public int getItemCount() {
        return mBusTerminals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.bus_terminal_id) TextView busTerminalId;
        @Bind(R.id.bus_terminal_address) TextView busTerminalAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.available_lines)
        public void showAvailableLines() {
            final BusTerminalParse busTerminal = mBusTerminals.get(getAdapterPosition());

            final String jsonArray = busTerminal.getLinhas().toString();
            final Type type = new TypeToken<List<BaseBusStopParse.BusLine>>() {}.getType();

            List<BaseBusStopParse.BusLine> lines = mGson.fromJson(jsonArray, type);
        }
    }
}
