package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Post - модель данных топика с сайта http://sql.ru/forum/job-offers
 * Класс {@link Description} - загружает подробное описание объявлений.
 *
 * @author Enver Eskendarov
 * @version 1.0 03/05/2021
 */
public class Post {

    private final String topic;
    private final String link;
    private final LocalDateTime topicDateTime;

    public Post(String topic, LocalDateTime topicDateTime, String link) {
        this.topic = topic;
        this.topicDateTime = topicDateTime;
        this.link = link;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDateTime getLocalDateTime() {
        return topicDateTime;
    }

    public String getLink() {
        return link;
    }

    public Description getDescription() {
        return new Description(link);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", topic, topicDateTime, link);
    }

    static class Description {

        private String text;
        private LocalDateTime localDateTime;

        public Description(String link) {
            try {
                final Document doc = Jsoup.connect(link).get();
                this.text = doc.select(".msgBody").get(1).text();
                this.localDateTime = new SqlRuDateTimeParser()
                        .parse(doc.select(".msgFooter")
                                .first()
                                .ownText()
                                .split(" \\[")[0]
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getText() {
            return text;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        @Override
        public String toString() {
            return String.format("%s (%s)", text, localDateTime);
        }
    }
}
