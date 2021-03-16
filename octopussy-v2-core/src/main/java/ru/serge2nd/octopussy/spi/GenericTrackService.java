package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Publisher;

/**
 * Creates, obtains, removes {@link GenericTrack} instances.
 */
public interface GenericTrackService {

    Publisher<GenericTrack> one(String id);

    Publisher<GenericTrack> find(String id);

    Publisher<GenericTrack> all();

    Publisher<GenericTrack> create(GenericTrack origin);

    Publisher<Void> delete(String id);

    Publisher<Void> close();
}
