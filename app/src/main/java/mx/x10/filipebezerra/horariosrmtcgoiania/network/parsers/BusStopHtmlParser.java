package mx.x10.filipebezerra.horariosrmtcgoiania.network.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

/**
 * @author Filipe Bezerra
 */
public class BusStopHtmlParser implements HtmlParser<FavoriteBusStop> {

    @Override
    public FavoriteBusStop parse(final String html) {
        Document doc = Jsoup.parse(html);

        Element tdNumPonto = doc.getElementsByClass("num-ponto").first();
        Element pNumPonto = tdNumPonto.getElementsByTag("p").first();
        String numPonto = pNumPonto.text();

        Element rowEndereco = doc.getElementsByClass("row").last();
        Element pEndereco = rowEndereco.getElementsByTag("p").first();
        pEndereco.select("strong").remove();
        String htmlEndereco = pEndereco.html();
        String [] split = htmlEndereco.split("<br>");

        String endereco = null, referencia = null;

        if (split.length > 0) {
            if (split.length == 2)
                referencia = split[1];

            endereco = split[0];
        }

        // TODO : create a builder
        FavoriteBusStop favoriteBusStop = new FavoriteBusStop();
        favoriteBusStop.setStopCode(Integer.valueOf(numPonto));
        favoriteBusStop.setAddress(endereco);
        favoriteBusStop.setStopReference(referencia);

        return favoriteBusStop;
    }

}
