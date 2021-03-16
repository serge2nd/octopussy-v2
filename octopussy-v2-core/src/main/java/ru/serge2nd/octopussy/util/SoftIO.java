package ru.serge2nd.octopussy.util;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;

import static java.lang.invoke.MethodHandles.lookup;
import static ru.serge2nd.ObjectAssist.errNotInstantiable;

public class SoftIO {
    private SoftIO() { throw errNotInstantiable(lookup()); }

    @SneakyThrows
    public static Connection getConnection(DataSource dataSource) {
        return dataSource.getConnection();
    }

    @SneakyThrows
    public static Connection getConnection(DataSource dataSource, String user, String password) {
        return dataSource.getConnection(user, password);
    }

    @SneakyThrows
    public static void close(Closeable c) { c.close(); }
}
