package mx.x10.filipebezerra.horariosrmtcgoiania.parsers;

/**
 * @author Filipe Bezerra
 */
public interface HtmlParser<T> {

    T parse(final String html);

}