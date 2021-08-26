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

    private int id;
    private String name;
    private String text;
    private String link;
    private LocalDateTime created;
    private LocalDateTime posted;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Post post = (Post) o;
        return Objects.equals(getName(), post.getName())
                && Objects.equals(getText(), post.getText())
                && Objects.equals(getLink(), post.getLink())
                && Objects.equals(getCreated(), post.getCreated())
                && Objects.equals(getPosted(), post.getPosted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getText(),
                getLink(),
                getCreated(),
                getPosted()
        );
    }

    @Override
    public String toString() {
        return String.format(
                "id: %s, name: %s, text: %s, link: %s, created: %s, posted: %s",
                id, name, text, link, created, posted
        );
    }

    public static class Builder {

        private final Post post;

        public Builder() {
            post = new Post();
        }

        /**
         * @param id - первичный ключ
         */
        public Builder setId(int id) {
            post.id = id;
            return this;
        }

        /**
         * @param name - имя вакансии
         */
        public Builder setName(String name) {
            post.name = name;
            return this;
        }

        /**
         * @param text - текст вакансии
         */
        public Builder setText(String text) {
            post.text = text;
            return this;
        }

        /**
         * @param link - ссылка на вакансию
         */
        public Builder setLink(String link) {
            post.link = link;
            return this;
        }

        /**
         * @param created - дата первого поста
         */
        public Builder setCreated(LocalDateTime created) {
            post.created = created;
            return this;
        }

        /**
         * @param posted - дата публикации поста
         */
        public Builder setPosted(LocalDateTime posted) {
            post.posted = posted;
            return this;
        }

        public Post build() {
            return post;
        }
    }
}
