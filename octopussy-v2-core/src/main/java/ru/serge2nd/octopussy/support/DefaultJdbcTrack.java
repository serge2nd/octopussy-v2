package ru.serge2nd.octopussy.support;

import lombok.NonNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.serge2nd.octopussy.spi.GenericTrack;
import ru.serge2nd.octopussy.spi.JdbcTrack;
import ru.serge2nd.octopussy.util.SoftIO;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;

import static ru.serge2nd.ObjectAssist.nullSafe;

public class DefaultJdbcTrack extends AbstractGenericTrack implements JdbcTrack {
    private final Mono<DataSource> dataSrcMono;
    private final Mono<Connection> connMono;

    public DefaultJdbcTrack(GenericTrack origin,
                            @NonNull Publisher<DataSource> dataSrc,
                            @NonNull Publisher<Connection> conn,
                            @NonNull Scheduler schdlr) {
        super(nullSafe(origin, "null origin").getId(), origin.getDefinition());
        this.dataSrcMono = Mono.from(dataSrc).subscribeOn(schdlr);
        this.connMono = Mono.from(conn).subscribeOn(schdlr);
    }

    @Override
    public Mono<DataSource> dataSource() { return dataSrcMono; }

    @Override
    public Mono<Connection> connection() {
        return connMono.switchIfEmpty(Mono.from(JdbcTrack.super.connection()));
    }

    @Override
    public Mono<Connection> connection(String user, String password) {
        return connMono.switchIfEmpty(Mono.from(JdbcTrack.super.connection(user, password)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Publisher<T> unwrap(Class<? extends T> as) {
        if (DataSource.class == as) return Mono.from(dataSource()).cast((Class<T>)as);
        if (Connection.class == as) return Mono.from(connection()).cast((Class<T>)as);
        return super.unwrap(as);
    }

    @Override
    public Mono<Void> close() {
        return dataSrcMono.doOnNext(dataSrc -> {
            if (dataSrc instanceof Closeable) SoftIO.close(((Closeable)dataSrc));
        }).then();
    }
}
