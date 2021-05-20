package ru.job4j.grabber;

import ru.job4j.html.Post;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.Store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * PsqlStore Class записывает данные с сайта sql.ru/forum/job-offers/ в БД.
 *
 * @author Enver Eskendarov
 * @version 1.0 08/05/2021
 */
public class PsqlStore implements Store, AutoCloseable {

    private Connection connection;

    public PsqlStore() {
        init();
    }

    public PsqlStore(Connection connection) {
        this.connection = connection;
    }

    public void init() {
        final ResourceBundle res = ResourceBundle.getBundle("grabber");
        try {
            connection = DriverManager.getConnection(
                    res.getString("url"),
                    res.getString("username"),
                    res.getString("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "insert into post(name, text, link, created, posted) "
                        + "values (?, ?, ?, ?, ?)")
        ) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getText());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.setTimestamp(5, Timestamp.valueOf(post.getPosted()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        final List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("select * from post")
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post.Builder()
                            .setId(resultSet.getInt("id"))
                            .setName(resultSet.getString("name"))
                            .setText(resultSet.getString("text"))
                            .setLink(resultSet.getString("link"))
                            .setCreated(resultSet.getTimestamp("created")
                                    .toLocalDateTime())
                            .setPosted(resultSet.getTimestamp("posted")
                                    .toLocalDateTime())
                            .build()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from post where id = ?")
        ) {
            statement.setInt(1, Integer.parseInt(id));
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                post = new Post.Builder()
                        .setId(resultSet.getInt("id"))
                        .setName(resultSet.getString("name"))
                        .setText(resultSet.getString("text"))
                        .setLink(resultSet.getString("link"))
                        .setCreated(resultSet.getTimestamp("created")
                                .toLocalDateTime())
                        .setPosted(resultSet.getTimestamp("posted")
                                .toLocalDateTime())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        final PsqlStore psqlStore = new PsqlStore();
        final SqlRuParse parse = new SqlRuParse();
        final List<Post> posts = parse
                .list("https://www.sql.ru/forum/job-offers/");
        posts.forEach(post -> psqlStore.save(parse.detail(post.getLink())));
        psqlStore.getAll().forEach(System.out::println);
        System.out.println(psqlStore.findById("27"));
    }
}
