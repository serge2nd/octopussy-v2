package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import ru.serge2nd.octopussy.util.SoftIO;

import javax.sql.DataSource;
import java.sql.Connection;

import static ru.serge2nd.octopussy.util.SoftIO.getConnection;

/**
 * @see GenericTrack
 */
public interface JdbcTrack extends GenericTrack {

    Publisher<DataSource> dataSource();

    default Publisher<Connection> connection() {
        return Mono.from(dataSource()).map(SoftIO::getConnection);
    }
    default Publisher<Connection> connection(String user, String password) {
        return Mono.from(dataSource()).map(dataSrc -> getConnection(dataSrc, user, password));
    }
}
