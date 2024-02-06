package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

/**
 * Map from one entity to another
 * We are using this mappers map between the business models and database entities
 *
 * @param <E> database type
 * @param <M> business entity type
 */
public interface EntityMapper<E, M> {

    /**
     * Map database entity to business entity
     *
     * @param entity database entity
     * @return business entity
     */
    M mapToBusiness(E entity);

    /**
     * Map new business entity to database entity
     *
     * @param entity business entity
     * @return database entity
     */
    E mapNewEntityToDatabase(M entity);

    /**
     * Map existing business entity to database entity
     *
     * @param entity existing business entity
     * @param dbId existing entity db id
     * @return database entity
     */
    E mapExistingEntityToDatabase(M entity, Long dbId);
}
