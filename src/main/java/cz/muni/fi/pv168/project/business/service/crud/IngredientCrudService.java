package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.DuplicateValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.DataStorageException;
import cz.muni.fi.pv168.project.ui.dialog.DeletePopupDialog;

import java.util.Collection;
import java.util.List;

public class IngredientCrudService implements CrudService<Ingredient> {

    private final Repository<Ingredient> ingredientRepository;
    private final Validator<Ingredient> ingredientValidator;
    private final GuidProvider guidProvider;
    private final Validator<Ingredient> ingredientUsageValidator;
    private final Validator<Ingredient> duplicateValidator;

    public IngredientCrudService(
            Repository<Ingredient> ingredientRepository,
            Validator<Ingredient> ingredientValidator,
            GuidProvider guidProvider,
            Validator<Ingredient> ingredientUsageValidator) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientValidator = ingredientValidator;
        this.guidProvider = guidProvider;
        this.ingredientUsageValidator = ingredientUsageValidator;
        this.duplicateValidator = new DuplicateValidator<>(ingredientRepository);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public ValidationResult create(Ingredient newEntity) {
        ValidationResult validationResult = ingredientValidator
                .and(duplicateValidator)
                .validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (ingredientRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException(
                    "Ingredient with given guid already exists: \" + newEntity.getGuid()"
            );
        }

        if (validationResult.isValid()) {
            newEntity.setGuid(guidProvider.newGuid());
            ingredientRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Ingredient entity) {
        ValidationResult validationResult = ingredientValidator.validate(entity);
        if (validationResult.isValid()) {
            ingredientRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult deleteByGuid(String guid, boolean userAgreed) {
        Ingredient toDelete = ingredientRepository.findByGuid(guid)
                .orElseThrow(
                        () -> new DataStorageException("Ingredient with guid: " + guid + "not found!")
                );
        ValidationResult validationResult = ingredientUsageValidator.validate(toDelete);

        DeletePopupDialog deletePopupDialog = new DeletePopupDialog(
                validationResult.isValid() ? List.of() : List.of(validationResult.toString())
        );

        if (deletePopupDialog.show().isValid()) {
            ingredientRepository.deleteByGuid(guid);
            return ValidationResult.success();
        }
        return ValidationResult.failed("denied");
    }

    @Override
    public ValidationResult deleteMultipleByGuids(Collection<String> guids) {
        List<String> invalidValResults = guids.stream()
                .map(guid -> ingredientRepository.findByGuid(guid)
                        .orElseThrow(
                                () -> new DataStorageException("Ingredient with guid: " + guid + "not found!"))
                )
                .map(ingredientUsageValidator::validate)
                .filter(valResult -> !valResult.isValid())
                .map(ValidationResult::toString)
                .toList();

        DeletePopupDialog deletePopupDialog = new DeletePopupDialog(invalidValResults);

        if (deletePopupDialog.show().isValid()) {
            guids.forEach(ingredientRepository::deleteByGuid);
            return ValidationResult.success();
        }
        return ValidationResult.failed("denied");
    }

    @Override
    public void deleteAll() {
        ingredientRepository.deleteAll();
    }
}
