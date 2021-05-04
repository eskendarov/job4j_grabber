package ru.job4j.html;

import java.time.LocalDateTime;

/**
 * Post - модель данных топика с сайта http://sql.ru/forum/job-offers
 *
 * @author Enver Eskendarov
 * @version 1.0 03/05/2021
 */
public class Post {

    private final String text;
    private final String link;
    private final LocalDateTime dateTime;

    public Post(String text, LocalDateTime dateTime, String link) {
        this.text = text;
        this.dateTime = dateTime;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", text, dateTime, link);
    }
}
