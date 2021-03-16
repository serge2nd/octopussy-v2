package ru.serge2nd.octopussy.spi;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;
import java.util.Map;

/**
 * @see GenericTrack
 */
public interface JpaTrack {

    Publisher<EntityManagerFactory> entityManagerFactory();

    default Publisher<EntityManager> entityManager() {
        return Mono.from(entityManagerFactory()).map(EntityManagerFactory::createEntityManager);
    }
    default Publisher<EntityManager> entityManager(SynchronizationType syncType) {
        return Mono.from(entityManagerFactory()).map(emf -> emf.createEntityManager(syncType));
    }
    @SuppressWarnings("rawtypes")
    default Publisher<EntityManager> entityManager(Map map) {
        return Mono.from(entityManagerFactory()).map(emf -> emf.createEntityManager(map));
    }
    @SuppressWarnings("rawtypes")
    default Publisher<EntityManager> entityManager(SynchronizationType syncType, Map map) {
        return Mono.from(entityManagerFactory()).map(emf -> emf.createEntityManager(syncType, map));
    }
}
