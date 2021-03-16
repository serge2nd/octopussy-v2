package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface Wrap {

    default <T> Publisher<T> unwrap(Class<? extends T> as) {
        if (as.isInstance(this)) return Mono.just(as.cast(this));
        throw errUnwrap(getClass(), as);
    }

    static IllegalArgumentException errUnwrap(Class<?> src, Class<?> as) {
        return new IllegalArgumentException("cannot unwrap " + as.getName() + " from " + src.getName());
    }
}
