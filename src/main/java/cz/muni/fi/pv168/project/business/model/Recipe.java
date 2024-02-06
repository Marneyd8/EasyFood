package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.business.service.crud.AddedIngredientCrudService;
import cz.muni.fi.pv168.project.ui.MainWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Recipe extends Entity {
    private int portions;
    private Category category;
    private String description = "No recipe description";
    private final AddedIngredientCrudService addedIngredientCrudService = MainWindow
            .commonDependencyProvider
            .getAddedIngredientCrudService();
    private List<AddedIngredient> addedIngredients = new ArrayList<>();

    private int prepMinutes;

    public Recipe() {
    }

    public Recipe(
            String guid,
            String recipeName,
            Category category,
            int prepMinutes,
            int portions,
            String description
    ) {
        super(guid);
        this.name = recipeName;
        this.category = category;
        this.prepMinutes = prepMinutes;
        this.portions = portions;
        this.description = description;
    }

    public String getRecipeName() {
        return super.name;
    }

    public void setRecipeName(String recipeName) {
        super.name = recipeName;
    }


    public void setPortions(int portions) {
        this.portions = portions;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }


    public int getPortions() {
        return portions;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "";
    }

    public Color getCategoryColor() {
        return category != null ? category.getColor() : Color.black;
    }

    public double getRecipeNutritionalValue() {
        double val = 0;
        for (var ingr : getAddedIngredients()) {
            val += (ingr.getIngredient().getNutritionalValue() * ingr.getQuantity());
        }
        return val;
    }


    public void addIngredient(AddedIngredient addedIngredient) {
        addedIngredients.add(addedIngredient);
    }

    public void removeIngredient(AddedIngredient addedIngredient) {
        addedIngredients.remove(addedIngredient);
    }

    public List<AddedIngredient> getAddedIngredients() {
        if (addedIngredients.isEmpty()) {
            addedIngredients = new ArrayList<>(addedIngredientCrudService.findByRecipeGuid(this.getGuid()));
        }
        return addedIngredients;
    }

    public int getPrepMinutes() {
        return prepMinutes;
    }

    public void setPrepMinutes(int prepMinutes) {
        this.prepMinutes = prepMinutes;
    }

    @Override
    public String toString() {
        return name;
    }
}
