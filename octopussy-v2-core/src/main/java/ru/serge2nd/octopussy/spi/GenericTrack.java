package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Publisher;

import java.util.Map;

/**
 * A generic track represents an identifiable resource or an identifiable group of resources that one can manage as one whole.
 * Has a static definition readable by {@link #getDefinition()}. That definition is sufficient to recreate the same track from it.<br>
 * Each track should be closed via {@link #close()} after all the interactions with it are finished.
 */
public interface GenericTrack extends Wrap {

    /**
     * The track ID.
     */
    String getId();

    /**
     * The fixed immutable definition of this track.
     */
    Map<String, ?> getDefinition();

    /**
     * Destroys this track by closing all the closeable components.
     */
    Publisher<Void> close();
}
