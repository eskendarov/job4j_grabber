package ru.job4j.html;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Post - модель данных топика с сайта http://sql.ru/forum/job-offers
 *
 * @author Enver Eskendarov
 * @version 1.0 03/05/2021
 */
public class Post {

    private final String topic;
    private final String link;
    private final LocalDateTime dateTime;

    public Post(String topic, LocalDateTime dateTime, String link) {
        this.topic = topic;
        this.dateTime = dateTime;
        this.link = link;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        final DateTimeFormatter timeFormatter = DateTimeFormatter
                .ofPattern("EEE, d MMM yy, HH:mm");
        return String.format("%s (%s) [%s]",
                topic, dateTime.format(timeFormatter), link
        );
    }
}
