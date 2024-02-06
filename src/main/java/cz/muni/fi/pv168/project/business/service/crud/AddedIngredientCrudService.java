package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.AddedIngredientSqlRepository;

import java.util.Collection;
import java.util.List;

public class AddedIngredientCrudService implements CrudService<AddedIngredient> {

    private final AddedIngredientSqlRepository addedIngredientRepository;
    private final Validator<AddedIngredient> addedIngredientValidator;
    private final GuidProvider guidProvider;

    public AddedIngredientCrudService(
            AddedIngredientSqlRepository addedIngredientRepository,
            Validator<AddedIngredient> addedIngredientValidator,
            GuidProvider guidProvider
    ) {
        this.addedIngredientRepository = addedIngredientRepository;
        this.addedIngredientValidator = addedIngredientValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<AddedIngredient> findAll() {
        return addedIngredientRepository.findAll();
    }

    public List<AddedIngredient> findByRecipeGuid(String recipeGuid) {
        return addedIngredientRepository.findByRecipeGuid(recipeGuid);
    }

    @Override
    public ValidationResult create(AddedIngredient newEntity) {
        ValidationResult validationResult = addedIngredientValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (addedIngredientRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException(
                    "Ingredient with given guid already exists: \" + newEntity.getGuid()"
            );
        }

        if (validationResult.isValid()) {
            newEntity.setGuid(guidProvider.newGuid());
            addedIngredientRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(AddedIngredient entity) {
        ValidationResult validationResult = addedIngredientValidator.validate(entity);
        if (validationResult.isValid()) {
            addedIngredientRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult deleteByGuid(String guid, boolean userAgreed) {
        addedIngredientRepository.deleteByGuid(guid);
        return ValidationResult.success();
    }

    @Override
    public ValidationResult deleteMultipleByGuids(Collection<String> guids) {
        guids.forEach(addedIngredientRepository::deleteByGuid);
        return ValidationResult.success();
    }

    @Override
    public void deleteAll() {
        addedIngredientRepository.deleteAll();
    }
}
