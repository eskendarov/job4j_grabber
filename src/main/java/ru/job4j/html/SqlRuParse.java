package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.time.format.DateTimeFormatter;

/**
 * SqlRuParse извлекает текст из HTML по аттрибутам тегов HTML.
 *
 * @author Enver Eskendarov
 * @version 1.0 29/03/2021
 */
public class SqlRuParse {

    public static void main(String[] args) throws Exception {
        final Document doc = Jsoup
                .connect("https://www.sql.ru/forum/job-offers").get();
        final Elements row = doc.select(".postslisttopic");
        final Elements date = doc.select("td[style].altCol");
        for (int i = 0; i < row.size(); i++) {
            final Element href = row.get(i).child(0);
            System.out.println(href.attr("href"));
            System.out.printf("%s (%s)%n",
                    href.text(),
                    new SqlRuDateTimeParser()
                            .parse(date.get(i).text())
                            .format(DateTimeFormatter
                                    .ofPattern("EEE, d MMM yy HH:mm")
                            )
            );
        }
    }
}
