package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SqlRuParse извлекает текст из HTML по аттрибутам тегов HTML.
 *
 * @author Enver Eskendarov
 * @version 1.0 29/03/2021
 */
public class SqlRuParse implements Parse {

    private final Map<Integer, Post> postMap = new LinkedHashMap<>();

    private Document getDocument(final String link) {
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public List<Post> list(final String link) {
        final Document doc = getDocument(link);
        final Elements topics = doc.select(".postslisttopic");
        final Elements id = doc.select(".altCol:nth-child(3)");
        final Elements datePosted = doc.select(".altCol:nth-child(6)");
        for (int i = 0; i < topics.size(); i++) {
            final String name = topics.get(i).text();
            final String topicLink = topics.get(i).child(0).attr("href");
            final int userId = Integer.parseInt(
                    id.get(i).child(0).attr("href").split("=")[1]
            );
            final LocalDateTime dateTime = new SqlTimeParser()
                    .parse(datePosted.get(i).text());
            final Post post = new Post(
                    userId, name, null, topicLink, null, dateTime
            );
            postMap.put(userId, post);
        }
        return new ArrayList<>(postMap.values());
    }

    @Override
    public Post detail(String link) {
        final Document doc = getDocument(link);
        final int userId = Integer.parseInt(doc.select(".msgBody")
                .first().child(0).attr("href").split("=")[1]
        );
        final String text = doc.select(".msgBody").get(1).text();
        final LocalDateTime created = new SqlTimeParser()
                .parse(doc.select(".msgFooter")
                        .get(0).text().split(" \\[")[0]
                );
        final Post post = postMap.get(userId);
        return new Post(
                post.getId(),
                post.getName(),
                text,
                post.getLink(),
                created,
                post.getPosted()
        );
    }
}
