package mx.x10.filipebezerra.horariosrmtcgoiania.parser;

/**
 * @author Filipe Bezerra
 */
public interface HtmlParser<T> {

    T parse(final String html);

}