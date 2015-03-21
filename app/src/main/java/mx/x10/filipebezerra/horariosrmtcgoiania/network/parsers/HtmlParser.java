package mx.x10.filipebezerra.horariosrmtcgoiania.network.parsers;

/**
 * @author Filipe Bezerra
 */
public interface HtmlParser<T> {

    T parse(final String html);

}