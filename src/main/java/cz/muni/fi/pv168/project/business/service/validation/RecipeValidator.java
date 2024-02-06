package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Recipe;

import static cz.muni.fi.pv168.project.business.service.validation.StringValidator.validateAlphaNumWhiteSpace;

public class RecipeValidator implements Validator<Recipe> {
    @Override
    public ValidationResult validate(Recipe model) {
        if (model.getCategory() == null) {
            return ValidationResult.failed(String.format("Recipe's: %s category is empty!", model.getName()));
        }
        if (model.getName().isEmpty()) {
            return ValidationResult.failed("Name is empty!");
        }
        if (!validateAlphaNumWhiteSpace(model.getName()).isValid()) {
            return ValidationResult.failed(
                    String.format("Recipe name format is invalid! Recipe name: %s", model.getName())
            );
        }
        if (!validateAlphaNumWhiteSpace(model.getCategoryName()).isValid()) {
            return ValidationResult.failed(
                    String.format("Category name format is invalid! Category name: %s", model.getName())
            );
        }
        if (model.getDescription().contains("<") || model.getDescription().contains(">")) {
            return ValidationResult.failed(
                    String.format("Description format is invalid! Description: %s", model.getDescription())
            );
        }
        return ValidationResult.success();
    }
}
