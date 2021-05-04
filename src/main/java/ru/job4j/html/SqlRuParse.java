package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SqlRuParse извлекает текст из HTML по аттрибутам тегов HTML.
 *
 * @author Enver Eskendarov
 * @version 1.0 29/03/2021
 */
public class SqlRuParse implements Parse {

    @Override
    public List<Post> list(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Elements topics = doc.select(".postslisttopic");
        final Elements date = doc.select("td[style].altCol");
        final List<Post> posts = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            final Element href = topics.get(i).child(0);
            posts.add(new Post(
                    href.text(),
                    new SqlRuDateTimeParser().parse(date.get(i).text()),
                    href.attr("href"))
            );
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String text = doc.select(".msgBody").get(1).text();
        final String create = doc.select(".msgFooter").get(0).text()
                .split(" \\[")[0];
        return new Post(text, new SqlRuDateTimeParser().parse(create), link);
    }
}
