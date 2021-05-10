package ru.job4j.html;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Post - модель данных топика с сайта http://sql.ru/forum/job-offers
 *
 * @author Enver Eskendarov
 * @version 1.0 03/05/2021
 */
public class Post {

    private final int id;
    private final String name;
    private final String text;
    private final String link;
    private final LocalDateTime created;
    private final LocalDateTime posted;

    /**
     * @param id      - первичный ключ
     * @param name    - имя вакансии
     * @param text    - текст вакансии
     * @param link    - ссылка на вакансию
     * @param created - дата первого поста
     * @param posted  - дата публикации поста
     */
    public Post(
            final int id,
            final String name,
            final String text,
            final String link,
            final LocalDateTime created,
            final LocalDateTime posted
    ) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.link = link;
        this.created = created;
        this.posted = posted;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getPosted() {
        return posted;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        final Post post = (Post) o;
        return getId() == post.getId()
                && getLink().equals(post.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLink());
    }

    @Override
    public String toString() {
        return String.format(
                "id: %s, name: %s, text: %s, link: %s, created: %s, posted: %s",
                id, name, text, link, created, posted
        );
    }
}
