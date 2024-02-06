package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.model.*;
import cz.muni.fi.pv168.project.business.service.validation.CategoryValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.CustomUnitDialog;
import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.CustomUnitTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.wiring.CommonDependencyProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Optional;

public final class EditAction extends ContextAction {

    private final CommonDependencyProvider commonDependencyProvider;

    public EditAction(
            JTable recipeTable,
            JTable ingredientTable,
            JTable unitsTable,
            JTable categoryTable,
            CommonDependencyProvider commonDependencyProvider
    ) {
        super(recipeTable, ingredientTable, unitsTable, categoryTable, "", Icons.EDIT_ICON);
        this.commonDependencyProvider = commonDependencyProvider;
        putValue(SHORT_DESCRIPTION, "Edits selected items");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JTable activeTable = TabbedPanelContext.getActiveTable();
        int[] selectedRows = activeTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (activeTable.isEditing()) {
            activeTable.getCellEditor().cancelCellEditing();
        }
        switch (TabbedPanelContext.getActiveTab()) {
            case 0: {
                RecipeTableModel recipeTableModel = (RecipeTableModel) activeTable.getModel();
                int modelRow = activeTable.convertRowIndexToModel(selectedRows[0]);
                Recipe recipe = recipeTableModel.getEntity(modelRow);
                RecipeDialog dialog = new RecipeDialog(recipe,
                        (RecipeTableModel) recipeTable.getModel(),
                        (IngredientTableModel) ingredientTable.getModel(),
                        (CategoryTableModel) categoryTable.getModel(),
                        (CustomUnitTableModel) unitsTable.getModel());
                Optional<Recipe> optionalRecipe = dialog.show(recipeTable, "Edit Recipe", new RecipeValidator());
                if (optionalRecipe.isPresent()) {
                    Recipe newRecipe = optionalRecipe.get();
                    recipeTableModel.updateRow(newRecipe);
                }
                break;
            }
            case 1: {
                IngredientTableModel ingredientTableModel = (IngredientTableModel) activeTable.getModel();
                int modelRow = activeTable.convertRowIndexToModel(selectedRows[0]);
                Ingredient ingredient = ingredientTableModel.getEntity(modelRow);
                IngredientDialog dialog = new IngredientDialog(ingredient, (IngredientTableModel) ingredientTable.getModel(), commonDependencyProvider.getCustomUnitCrudService(), (RecipeTableModel) recipeTable.getModel());
                dialog.show(activeTable, "Edit Ingredient", new IngredientValidator())
                        .ifPresent(ingredientTableModel::updateRow);
                break;
            }
            case 2: {
                CustomUnitTableModel customUnitTableModel = (CustomUnitTableModel) activeTable.getModel();
                int modelRow = activeTable.convertRowIndexToModel(selectedRows[0]);
                CustomUnit unit = customUnitTableModel.getEntity(modelRow);
                CustomUnitDialog dialog = new CustomUnitDialog(unit, (CustomUnitTableModel) unitsTable.getModel());
                dialog.show(activeTable, "Edit Custom Unit", new UnitValidator())
                        .ifPresent(customUnitTableModel::updateRow);
                break;
            }
            case 3: {
                CategoryTableModel categoryTableModel = (CategoryTableModel) activeTable.getModel();
                int modelRow = activeTable.convertRowIndexToModel(selectedRows[0]);
                Category category = categoryTableModel.getEntity(modelRow);
                CategoryDialog categoryDialog = new CategoryDialog(category, (CategoryTableModel) categoryTable.getModel());
                categoryDialog.show(activeTable, "Edit Category", new CategoryValidator())
                        .ifPresent(categoryTableModel::updateRow);
            }
        }
        StatisticsUpdater.reload();

    }
}
