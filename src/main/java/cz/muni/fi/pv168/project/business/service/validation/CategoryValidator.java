package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Category;

import static cz.muni.fi.pv168.project.business.service.validation.StringValidator.validateAlphaNumWhiteSpace;


public class CategoryValidator implements Validator<Category> {
    @Override
    public ValidationResult validate(Category model) {

        if (model.getName().isEmpty()) {
            return ValidationResult.failed("Category name is empty!");
        }

        if (!validateAlphaNumWhiteSpace(model.getName()).isValid()) {
            return ValidationResult.failed(
                    String.format("Category name format is invalid! Category name: %s", model.getName())
            );
        }
        return ValidationResult.success();
    }
}
