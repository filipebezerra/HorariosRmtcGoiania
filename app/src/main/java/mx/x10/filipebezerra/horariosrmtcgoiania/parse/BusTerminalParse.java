package mx.x10.filipebezerra.horariosrmtcgoiania.parse;

import com.parse.ParseClassName;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;

/**
 * Parse cloud data store representation of {@link BusStop} containing only bus terminals data.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.0
 */
@ParseClassName("BusTerminal")
public class BusTerminalParse extends BaseBusStopParse {
    public static final String LOCAL_NAME = "BusTerminals";

    public BusTerminalParse() {
        super();
    }
}
