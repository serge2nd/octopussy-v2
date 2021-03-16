package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Subscriber;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Publishes {@link GenericTrack}s for an exclusive access (<i>as a rule</i>).
 * @see org.reactivestreams.Publisher
 */
public interface GenericTrackPublisher {

    void subscribe(String id, Subscriber<? super GenericTrack> s, Scheduler schdlr);

    default void subscribe(String id, Subscriber<? super GenericTrack> s) {
        subscribe(id, s, Schedulers.immediate());
    }
}
