package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.CustomUnit;

import static cz.muni.fi.pv168.project.business.service.validation.StringValidator.*;

public class UnitValidator implements Validator<CustomUnit> {

    @Override
    public ValidationResult validate(CustomUnit model) {
        if (model.getName().isEmpty()) {
            return ValidationResult.failed("Unit name is empty!");
        }
        if (model.getAbbreviation().isEmpty()) {
            return ValidationResult.failed("Unit abbreviation is empty!");
        }
        if (!validateAlphaNumWhiteSpace(model.getName()).isValid()) {
            return ValidationResult.failed(
                    String.format("Unit name format is invalid! Unit name: %s", model.getName())
            );
        }
        if (!validateAlphaNum(model.getAbbreviation()).isValid()) {
            return ValidationResult.failed(
                    String.format(
                            "Unit abbreviation name format is invalid! Unit abbr. name: %s",
                            model.getAbbreviation()
                    )
            );
        }
        if (!validateDouble(model.getBaseAmountNumber()).isValid()) {
            return ValidationResult.failed(
                    String.format("Unit amount format is invalid! Unit amount: %f", model.getAmount())
            );
        }
        if (!validateAlphaNum(model.getBaseUnit().getAbbreviation()).isValid()) {
            return ValidationResult.failed("System err: Base units abbreviation is invalid! Please fix your db!");
        }

        return ValidationResult.success();
    }

}
