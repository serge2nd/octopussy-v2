package ru.serge2nd.octopussy.support;

import lombok.NonNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.serge2nd.octopussy.spi.GenericTrack;
import ru.serge2nd.octopussy.spi.JpaTrack;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.function.Function;

public class DefaultJpaTrack extends DefaultJdbcTrack implements JpaTrack {
    private final Mono<EntityManagerFactory> emfMono;

    public DefaultJpaTrack(GenericTrack origin,
                           Publisher<DataSource> dataSrc,
                           @NonNull Function<? super Mono<DataSource>, ? extends Publisher<EntityManagerFactory>> toEmf,
                           Scheduler schdlr) {
        super(origin, dataSrc, Mono.empty(), schdlr);
        this.emfMono = dataSource().transform(toEmf).subscribeOn(schdlr);
    }

    @Override
    public Mono<EntityManagerFactory> entityManagerFactory() { return emfMono; }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Publisher<T> unwrap(Class<? extends T> as) {
        if (EntityManagerFactory.class == as) return entityManagerFactory().cast((Class<T>)as);
        return super.unwrap(as);
    }

    @Override
    public Mono<Void> close() {
        return emfMono
            .doOnNext(EntityManagerFactory::close)
            .then(super.close());
    }
}
