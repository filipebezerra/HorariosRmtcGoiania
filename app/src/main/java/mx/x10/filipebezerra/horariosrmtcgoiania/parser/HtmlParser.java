package mx.x10.filipebezerra.horariosrmtcgoiania.parser;

/**
 * Created by Thiago Souza on 09/02/2015.
 */
public interface HtmlParser<T> {

    T parse(final String html);

}