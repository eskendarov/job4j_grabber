package ru.job4j.grabber;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection, which rollback all commits. It's used for integration test.
 *
 * @author Enver Eskendarov
 * @version 1.0 20/05/2021
 */
public class ConnectionRollback {

    /**
     * Create connection with autocommit=false mode and rollback call,
     * when connection is closed.
     *
     * @param connection connection.
     * @return Connection object.
     * @throws java.sql.SQLException possible exception.
     */
    public static Connection create(Connection connection)
            throws SQLException {
        connection.setAutoCommit(false);
        return (Connection) Proxy.newProxyInstance(
                ConnectionRollback.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    Object rsl = null;
                    if ("close".equals(method.getName())) {
                        connection.rollback();
                        connection.close();
                    } else {
                        rsl = method.invoke(connection, args);
                    }
                    return rsl;
                }
        );
    }
}
