package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.service.crud.AddedIngredientCrudService;
import cz.muni.fi.pv168.project.ui.MainWindow;

import java.util.List;


public class AddedIngredientsTableModel extends AbstractEntityTableModel<AddedIngredient> {

    private final AddedIngredientCrudService addedIngredientCrudService;

    public AddedIngredientsTableModel(
            String recipeGuid,
            AddedIngredientCrudService addedIngredientCrudService) {
        super(List.of(
                Column.readonly("Ingredient", Ingredient.class, AddedIngredient::getIngredient),
                Column.readonly("amount", Double.class, AddedIngredient::getQuantity),
                Column.readonly("Unit", Unit.class, AddedIngredient::getUnit)
        ), addedIngredientCrudService.findByRecipeGuid(recipeGuid), MainWindow.commonDependencyProvider.getAddedIngredientCrudService());
        this.addedIngredientCrudService = addedIngredientCrudService;
    }

    @Override
    public void addRow(AddedIngredient entity) {
        super.getEntities().add(entity);
        fireTableRowsInserted(super.getEntities().size() - 1, super.getEntities().size() - 1);
    }

    @Override
    public void deleteRow(int rowIndex) {
        AddedIngredient entity = getEntity(rowIndex);
        entities.remove(rowIndex);
        if (entity.getGuid() != null) {
            crudService.deleteByGuid(entity.getGuid(), true);
        }
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
