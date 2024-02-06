package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.DuplicateValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.DataStorageException;
import cz.muni.fi.pv168.project.ui.dialog.DeletePopupDialog;

import java.util.Collection;
import java.util.List;

public class UnitCrudService implements CrudService<CustomUnit> {

    private final Repository<CustomUnit> unitRepository;
    private final Validator<CustomUnit> customUnitValidator;
    private final GuidProvider guidProvider;
    private final Validator<CustomUnit> unitUsageValidator;
    private final Validator<CustomUnit> duplicityValidator;

    public UnitCrudService(
            Repository<CustomUnit> unitRepository,
            Validator<CustomUnit> customUnitValidator,
            GuidProvider guidProvider,
            Validator<CustomUnit> unitUsageValidator
    ) {
        this.unitRepository = unitRepository;
        this.customUnitValidator = customUnitValidator;
        this.guidProvider = guidProvider;
        this.unitUsageValidator = unitUsageValidator;
        this.duplicityValidator = new DuplicateValidator<>(unitRepository);
    }


    @Override
    public List<CustomUnit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    public ValidationResult create(CustomUnit newEntity) {
        ValidationResult validationResult = customUnitValidator
                .and(duplicityValidator)
                .validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (unitRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException(
                    "CustomUnit with given guid already exists: \" + newEntity.getGuid()"
            );
        }

        if (validationResult.isValid()) {
            newEntity.setGuid(guidProvider.newGuid());
            unitRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(CustomUnit entity) {
        ValidationResult validationResult = customUnitValidator.validate(entity);
        if (validationResult.isValid()) {
            unitRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult deleteByGuid(String guid, boolean userAgreed) {
        CustomUnit toDelete = unitRepository.findByGuid(guid)
                .orElseThrow(
                        () -> new DataStorageException("Custom unit with guid: " + guid + "not found!")
                );
        ValidationResult validationResult = unitUsageValidator.validate(toDelete);

        DeletePopupDialog deletePopupDialog = new DeletePopupDialog(
                validationResult.isValid() ? List.of() : List.of(validationResult.toString())
        );

        if (deletePopupDialog.show().isValid()) {
            unitRepository.deleteByGuid(guid);
            return ValidationResult.success();
        }
        return ValidationResult.failed("denied");
    }

    @Override
    public ValidationResult deleteMultipleByGuids(Collection<String> guids) {
        List<String> invalidValResults = guids.stream()
                .map(guid -> unitRepository.findByGuid(guid)
                        .orElseThrow(
                                () -> new DataStorageException("Custom unit with guid: " + guid + "not found!"))
                )
                .map(unitUsageValidator::validate)
                .filter(valResult -> !valResult.isValid())
                .map(ValidationResult::toString)
                .toList();

        DeletePopupDialog deletePopupDialog = new DeletePopupDialog(invalidValResults);

        if (deletePopupDialog.show().isValid()) {
            guids.forEach(unitRepository::deleteByGuid);
            return ValidationResult.success();
        }
        return ValidationResult.failed("denied");
    }

    @Override
    public void deleteAll() {
        unitRepository.deleteAll();
    }
}
