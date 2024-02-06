package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnitUsageValidator implements Validator<CustomUnit> {
    private final Repository<AddedIngredient> addedIngredientRepository;

    public UnitUsageValidator(Repository<AddedIngredient> addedIngredientRepository) {
        this.addedIngredientRepository = addedIngredientRepository;
    }

    @Override
    public ValidationResult validate(CustomUnit model) {
        List<AddedIngredient> foundUsages = addedIngredientRepository.findAll().stream()
                .filter(ai -> ai.getUnit().getClass().equals(CustomUnit.class))
                .filter(ai -> ((CustomUnit) ai.getUnit()).getGuid().equals(model.getGuid()))
                .toList();
        if (!foundUsages.isEmpty()) {
            return ValidationResult.failed(
                    String.format("Unit: %s is used %d times", model, foundUsages.size()),
                    getRecipesFormattedString(foundUsages)
            );
        }
        return ValidationResult.success();
    }

    private String getRecipesFormattedString(List<AddedIngredient> addedIngredients) {
        StringBuilder sb = new StringBuilder("In recipes:" + System.lineSeparator());
        Set<Recipe> recipes = addedIngredients.stream()
                .map(AddedIngredient::getRecipe)
                .collect(Collectors.toSet());
        recipes.forEach(recipe -> sb.append("-> ").append(recipe).append(System.lineSeparator()));
        return sb.toString();
    }
}
