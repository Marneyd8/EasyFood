package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IngredientUsageValidator implements Validator<Ingredient> {
    private final Repository<AddedIngredient> addedIngredientRepository;

    public IngredientUsageValidator(Repository<AddedIngredient> addedIngredientRepository) {
        this.addedIngredientRepository = addedIngredientRepository;
    }

    @Override
    public ValidationResult validate(Ingredient model) {
        List<AddedIngredient> addedIngredients = addedIngredientRepository.findAll().stream()
                .filter(ai -> ai.getIngredient().getGuid().equals(model.getGuid()))
                .toList();
        if (!addedIngredients.isEmpty()) {
            return ValidationResult.failed(
                    String.format("Ingredient: %s is used %d times", model, addedIngredients.size()),
                    getRecipesFormattedString(addedIngredients)
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
