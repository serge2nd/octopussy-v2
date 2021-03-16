package ru.serge2nd.octopussy.spi;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * @see GenericTrack
 */
public interface R2dbcTrack extends GenericTrack {

    Publisher<ConnectionFactory> connectionFactory();

    default Publisher<Connection> connection() {
        return Mono.from(connectionFactory()).flatMapMany(ConnectionFactory::create);
    }
}
