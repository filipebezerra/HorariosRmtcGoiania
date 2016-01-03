package mx.x10.filipebezerra.horariosrmtcgoiania.parse;

import com.parse.ParseObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Container of {@link ParseObject}s related to domain classes.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.0
 */
public class ParseSubclasses {
    private static List<Class<? extends ParseObject>> list() {
        return Collections.unmodifiableList(Arrays.<Class<? extends ParseObject>>asList(
                BusStopParse.class,
                BusTerminalParse.class
        ));
    }

    public static void registerSubclasses() {
        for (Class<? extends ParseObject> subclass : list()) {
            ParseObject.registerSubclass(subclass);
        }
    }
}
