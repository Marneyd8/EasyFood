package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import java.util.List;

public class IngredientTableModel extends AbstractEntityTableModel<Ingredient> {
    private final CrudService<Ingredient> ingredientCrudService;

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService) {
        super(List.of(
                Column.readonly("Ingredient name", String.class, Ingredient::getName),
                Column.readonly("Nutritional value [KCAL]", int.class, Ingredient::getNutritionalValue)
        ), ingredientCrudService.findAll(), ingredientCrudService);
        this.ingredientCrudService = ingredientCrudService;
    }

    public Ingredient[] getArrayOfIngredients() {
        return ingredientCrudService.findAll().toArray(new Ingredient[0]);
    }

}