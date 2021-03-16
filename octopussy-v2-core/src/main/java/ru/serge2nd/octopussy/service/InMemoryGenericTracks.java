package ru.serge2nd.octopussy.service;

import lombok.NonNull;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.serge2nd.octopussy.spi.GenericTrack;
import ru.serge2nd.octopussy.spi.GenericTrackPublisher;
import ru.serge2nd.octopussy.spi.GenericTrackService;

import java.util.Map;
import java.util.function.UnaryOperator;

import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
import static ru.serge2nd.octopussy.service.ex.GenericTrackException.errTrackExists;
import static ru.serge2nd.octopussy.service.ex.GenericTrackException.errTrackNotFound;

public class InMemoryGenericTracks implements GenericTrackService, GenericTrackPublisher {
    private final Mono<Map<String, GenericTrack>> byId;
    private final UnaryOperator<GenericTrack> byOrigin;

    public InMemoryGenericTracks(@NonNull Map<String, GenericTrack> byId,
                                 @NonNull UnaryOperator<GenericTrack> byOrigin,
                                 @NonNull Scheduler schdlr) {
        this.byId = Mono.just(byId).subscribeOn(schdlr);
        this.byOrigin = byOrigin;
    }

    @Override
    public Mono<GenericTrack> one(String id)  { return find(id).switchIfEmpty(error(() -> errTrackNotFound(id))); }
    @Override
    public Mono<GenericTrack> find(String id) { return byId.map($ -> $.get(id)); }
    @Override
    public Flux<GenericTrack> all()           { return byId.flatMapIterable(Map::values); }

    @Override
    public void subscribe(String id, Subscriber<? super GenericTrack> s, Scheduler schdlr) {
        byId.publishOn(schdlr).map($ -> $.compute(id, (__, track) -> {
            Mono.just(ofNullable(track)
            .orElseThrow(() -> errTrackNotFound(id)))
            .subscribe(s);
            return track;
        })).subscribe();
    }

    @Override
    public Mono<GenericTrack> create(GenericTrack origin) {
        return byId.map($ -> $.compute(origin.getId(), (id, existing) ->
            ofNullable(existing == null ? origin : null)
            .map(byOrigin)
            .orElseThrow(() -> errTrackExists(id))));
    }

    @Override
    public Mono<Void> delete(String id) {
        return byId.doOnNext($ -> $.compute(id, (__, track) ->
            Mono.just(ofNullable(track)
            .orElseThrow(() -> errTrackNotFound(id)))
            .delayUntil(GenericTrack::close)
            .ignoreElement().block()))
            .then();
    }

    @Override
    public Mono<Void> close() {
        return all().flatMap(GenericTrack::close).then();
    }
}
