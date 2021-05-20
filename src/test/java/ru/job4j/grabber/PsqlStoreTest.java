package ru.job4j.grabber;

import junit.framework.TestCase;
import ru.job4j.html.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PsqlStoreTest extends TestCase {

    private final Post post = new Post.Builder()
            .setId(22)
            .setName("Post Name")
            .setText("text")
            .setLink("http://sql.ru")
            .setCreated(LocalDateTime.parse("2021-05-20T05:54"))
            .setPosted(LocalDateTime.parse("2021-05-20T05:54"))
            .build();

    public Connection init() throws SQLException {
        final ResourceBundle res = ResourceBundle.getBundle("grabber");
        return DriverManager.getConnection(
                res.getString("url"),
                res.getString("username"),
                res.getString("password")
        );
    }

    public void testSave() throws Exception {
        try (PsqlStore psqlStore = new PsqlStore(
                ConnectionRollback.create(this.init()))
        ) {
            psqlStore.save(post);
            final Post expected = psqlStore.getAll().get(0);
            assertThat(post.getLink(), is(expected.getLink()));
        }
    }

    public void testGetAll() throws Exception {
        try (PsqlStore psqlStore = new PsqlStore(
                ConnectionRollback.create(this.init()))
        ) {
            psqlStore.save(post);
            assertThat(psqlStore.getAll().size(), is(1));
        }
    }

    public void testFindById() throws Exception {
        try (PsqlStore psqlStore = new PsqlStore(
                ConnectionRollback.create(this.init()))
        ) {
            psqlStore.save(post);
            final String id = String.valueOf(
                    psqlStore.getAll().get(0).getId()
            );
            assertThat(psqlStore.findById(id).getName(), is(post.getName()));
        }
    }
}
