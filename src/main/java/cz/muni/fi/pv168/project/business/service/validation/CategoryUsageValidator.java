package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;

import java.util.List;

public class CategoryUsageValidator implements Validator<Category> {

    private final Repository<Recipe> recipeRepository;

    public CategoryUsageValidator(
            Repository<Recipe> recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public ValidationResult validate(Category model) {
        List<Recipe> recipesWithThisModel = recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getCategory().getName().equals(model.getName()))
                .toList();

        if (!recipesWithThisModel.isEmpty()) {
            return ValidationResult.failed(
                    String.format("Category: %s is used %d times", model, recipesWithThisModel.size()),
                    getRecipesFormattedString(recipesWithThisModel)
            );
        }
        return ValidationResult.success();
    }

    private String getRecipesFormattedString(List<Recipe> recipes) {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        recipes.forEach(recipe -> sb.append("-> ").append(recipe).append(System.lineSeparator()));
        return sb.toString();
    }
}


