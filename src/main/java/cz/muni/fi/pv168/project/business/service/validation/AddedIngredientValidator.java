package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;


public class AddedIngredientValidator implements Validator<AddedIngredient> {
    @Override
    public ValidationResult validate(AddedIngredient model) {
        if (!StringValidator.validateAlphaNumWhiteSpace(model.getIngredient().getName()).isValid()) {
            return ValidationResult.failed(
                    String.format("Ingredient name format is invalid! Ingredient name: %s", model.getIngredient().getName())
            );
        }
        if (!StringValidator.validateAlphaNum(model.getUnit().getAbbreviation()).isValid()) {
            return ValidationResult.failed(
                    String.format("Unit abbr. format is invalid! Unit abbr. : %s", model.getUnit().getAbbreviation())
            );
        }
        return ValidationResult.success();
    }

}
