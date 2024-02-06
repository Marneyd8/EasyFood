package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import java.util.List;
import java.util.Optional;


public class IngredientDialog extends EntityDialog<Ingredient> {

    private int statistic = 0;
    private final JTextField nameField = new JTextField();
    private final JSpinner nutritionalValueSpinner = new JSpinner(
            new SpinnerNumberModel(0, 0, 50000, 20)
    );

    private final CrudService<CustomUnit> unitCrudService;

    private final RecipeTableModel recipeData;

    public IngredientDialog(
            Ingredient ingredient,
            IngredientTableModel ingredientTableModel,
            CrudService<CustomUnit> unitCrudService,
            RecipeTableModel recipeTableModel
    ) {
        super(ingredient, ingredientTableModel.getEntities());
        this.unitCrudService = unitCrudService;
        this.recipeData = recipeTableModel;
        if (ingredient != null) {
            statistic = countIngredientUsage();
            setFields();
        } else {
            entity = new Ingredient(null, 0); //BaseUnits.getBaseUnitList().get(0)
        }
        addFields();

    }

    private int countIngredientUsage() {
        int result = 0;
        List<Recipe> recipes = recipeData.getEntities();
        for (Recipe recipe : recipes) {
            Optional<AddedIngredient> addedIngredient = recipe.getAddedIngredients().stream()
                    .filter(ai -> ai.getIngredient().getGuid().equals(entity.getGuid()))
                    .findFirst();
            if (addedIngredient.isPresent()) {
                result++;
            }
        }
        return result;
    }

    private void addFields() {
        add("Name", nameField);
        add("Nutritional value [KCAL]", nutritionalValueSpinner);
        add("Ingredient used in " + statistic + " recipes");
    }

    private void setFields() {
        nameField.setText(entity.getName());
        nutritionalValueSpinner.setModel(new SpinnerNumberModel(entity.getNutritionalValue(), 0, 50000, 20));
    }

    @Override
    Ingredient getEntity() {
        Ingredient setEntity = new Ingredient();
        setEntity.setGuid(entity.getGuid());
        setEntity.setName(nameField.getText());
        setEntity.setNutritionalValue((int) nutritionalValueSpinner.getValue());
        return setEntity;
    }
}
