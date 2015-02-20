package mx.x10.filipebezerra.horariosrmtcgoiania.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

/**
 * Created by Thiago Souza on 09/02/2015.
 */
public class BusStopHtmlParser implements HtmlParser<FavoriteBusStop> {

    private static final String TAG = BusStopHtmlParser.class.getSimpleName();

    @Override
    public FavoriteBusStop parse(final String html) {
        Document doc = Jsoup.parse(html);
        FavoriteBusStop favoriteBusStop;

        Element tdNumPonto = doc.getElementsByClass("num-ponto").first();
        Element pNumPonto = tdNumPonto.getElementsByTag("p").first();
        final String numPonto = pNumPonto.text();
        Log.d(TAG, String.format("Num Ponto: %s", numPonto));

        Element tdLinhasPonto = doc.getElementsByClass("linhas-ponto").first();
        Element pLinhasPonto = tdLinhasPonto.getElementsByTag("p").first();
        final String linhasPonto = pLinhasPonto.text();
        Log.d(TAG, String.format("Linhas Ponto: %s", linhasPonto));

        Element rowEndereco = doc.getElementsByClass("row").last();
        Element pEndereco = rowEndereco.getElementsByTag("p").first();
        pEndereco.select("strong").remove();
        String htmlEndereco = pEndereco.html();
        String [] split = htmlEndereco.split("<br>");

        String endereco = split[0] != null ? split[0] : "";
        Log.d(TAG, String.format("Endereco: %s", endereco));

        String referencia = split[1] != null ? split[1] : "";
        Log.d(TAG, String.format("Referencia: %s", referencia));

        favoriteBusStop = new FavoriteBusStop();
        favoriteBusStop.setStopCode(Integer.valueOf(numPonto));
        favoriteBusStop.setAddress(endereco);
        favoriteBusStop.setStopReference(referencia);

        return favoriteBusStop;
    }

}
