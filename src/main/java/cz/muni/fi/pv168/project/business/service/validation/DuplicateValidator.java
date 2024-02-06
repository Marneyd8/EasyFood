package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

public class DuplicateValidator<E extends Entity> implements Validator<E>{

    private final Repository<E> repository;

    public DuplicateValidator(Repository<E> repository) {
        this.repository = repository;
    }

    @Override
    public ValidationResult validate(E model) {
        if (repository.existsByName(model.getName())) {
            return ValidationResult.failed(String.format(
                    "%s with name %s already exists!", model.getClass().getSimpleName(), model.getName()
            ));
        }
        return ValidationResult.success();
    }
}
