package cz.muni.fi.pv168.project.business.repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Represents a repository for any entity.
 *
 * @param <T> the type of the entity.
 */
public interface Repository<T> {

    /**
     * Find all entities.
     */
    List<T> findAll();

    /**
     * Find entity by its guid, if exists
     */
    Optional<T> findByGuid(String guid);
    Optional<T> findById(Long id);

    /**
     * Persist given {@code newEntity}.
     */
    void create(T newEntity);

    /**
     * Update given {@code entity}.
     */
    void update(T entity);

    /**
     * Delete entity with given {@code guid}.
     */
    void deleteByGuid(String guid);

    /**
     * Delete all entities.
     */
    void deleteAll();

    /**
     * Check if there is an existing Entity with given {@code guid}
     *
     * @return true, if an Entity with given {@code} is found, false otherwise
     */
    boolean existsByGuid(String guid);

    /**
     * Check if there is an existing Entity with name {@code name}
     *
     * @return true, if an Entity with given {@code} is found, false otherwise
     */
    boolean existsByName(String name);
}
