package ru.serge2nd.octopussy.support;

import io.r2dbc.spi.ConnectionFactory;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import ru.serge2nd.octopussy.spi.GenericTrack;
import ru.serge2nd.octopussy.spi.R2dbcTrack;

import static ru.serge2nd.ObjectAssist.nullSafe;

public class DefaultR2DbcTrack extends AbstractGenericTrack implements R2dbcTrack {
    private final Mono<ConnectionFactory> connFactoryMono;

    public DefaultR2DbcTrack(GenericTrack origin, @NonNull Publisher<ConnectionFactory> connFactory) {
        super(nullSafe(origin, "null origin").getId(), origin.getDefinition());
        this.connFactoryMono = Mono.from(connFactory).cache();
    }

    @Override
    public Mono<ConnectionFactory> connectionFactory() { return connFactoryMono; }

    @Override
    public Mono<Void> close() {
        return connFactoryMono.delayUntil(connFactory ->
            connFactory instanceof io.r2dbc.spi.Closeable
                ? Mono.from(((io.r2dbc.spi.Closeable)connFactory).close())
                : Mono.empty())
            .then();
    }
}
