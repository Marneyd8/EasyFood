package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;

import java.util.Collection;
import java.util.List;

public interface CrudService<T> {

    List<T> findAll();

    /**
     * Validate and store the given {@code newEntity}.
     *
     * @throws EntityAlreadyExistsException if there is already an existing entity with the same guid
     */
    ValidationResult create(T newEntity);

    /**
     * Updates the given {@code entity}.
     */
    ValidationResult update(T entity);

    /**
     * Delete entity with given guid.
     */
    ValidationResult deleteByGuid(String guid, boolean userAgreed);

    ValidationResult deleteMultipleByGuids(Collection<String> guids);

    /**
     * Delete all entities.
     */
    void deleteAll();
}
