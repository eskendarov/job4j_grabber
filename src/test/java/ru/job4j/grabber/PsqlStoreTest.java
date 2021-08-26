package ru.job4j.grabber;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.html.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PsqlStoreTest {

    private static Connection connection;
    private Post post;
    private PsqlStore store;

    @BeforeClass
    public static void initConnection() throws SQLException {
        final ResourceBundle resources = ResourceBundle.getBundle("test");
        connection = DriverManager.getConnection(
                resources.getString("url"),
                resources.getString("username"),
                resources.getString("password")
        );
    }

    @Before
    public void initData() {
        post = new Post.Builder()
                .setName("Post Name")
                .setText("text")
                .setLink("http://sql.ru")
                .setCreated(LocalDateTime.parse("2021-05-15T13:04"))
                .setPosted(LocalDateTime.parse("2021-05-20T05:54"))
                .build();
        store = new PsqlStore(connection);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void clearTable() throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("delete from post")
        ) {
            statement.execute();
        }
    }

    @Test
    public void testSave() {
        store.save(post);
        final Post expected = store.getAll().get(0);
        assertThat(post.getLink(), is(expected.getLink()));
    }

    @Test
    public void testGetAll() {
        store.save(post);
        assertThat(store.getAll().size(), is(1));
    }

    @Test
    public void testFindById() {
        store.save(post);
        final String id = String.valueOf(store.getAll().get(0).getId());
        assertThat(store.findById(id).getName(), is(post.getName()));
    }
}
