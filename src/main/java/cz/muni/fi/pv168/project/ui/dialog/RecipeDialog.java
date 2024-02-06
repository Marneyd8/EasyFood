package cz.muni.fi.pv168.project.ui.dialog;


import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.BaseUnit;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.ui.model.AddedIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.CustomUnitTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField recipeNameField = new JTextField();
    private final JSpinner recipePortionsField = new JSpinner(
            new SpinnerNumberModel(1, 1, 200, 1)
    );
    private final JSpinner amount = new JSpinner(
            new SpinnerNumberModel(1, 0, 100000, 0.1)
    );
    private final JTextField categoryNameField = new JTextField();
    private final JComboBox<Category> categoryJComboBox = new JComboBox<>();
    private final JColorChooser categoryColor = new JColorChooser();
    private final JSpinner timeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000000, 1));
    private final JComboBox<Ingredient> ingredients;
    private final AddedIngredientsTableModel addedIngredientsTableModel;
    private final JComboBox<Unit> units;
    private final JTextArea recipeDescriptionTextField = new JTextArea();
    private final JTable addedIngredientsTable = new JTable();


    private final JButton addIngredient = new JButton(new AbstractAction("Add ingredient") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (((double) amount.getValue()) == 0) {
                JOptionPane.showMessageDialog(null, "Amount should be positive number!");
                amount.setValue(1.0);
                return;
            }
            if (ingredients.getSelectedItem() == null || units.getSelectedItem() == null) {
                return;
            }

            AddedIngredient newIngredient = new AddedIngredient(
                    (Ingredient) ingredients.getSelectedItem(),
                    (double) amount.getValue(),
                    (Unit) units.getSelectedItem());
            if (entity.getGuid() != null) {
                newIngredient.setRecipe(entity);
                MainWindow.commonDependencyProvider.getAddedIngredientCrudService().create(newIngredient);
            }

            entity.addIngredient(newIngredient);
            addedIngredientsTableModel.addRow(newIngredient);
        }
    });
    private final JButton removeIngredient = new JButton(new AbstractAction("Remove ingredient") {
        @Override
        public void actionPerformed(ActionEvent e) {
            Arrays.stream(addedIngredientsTable.getSelectedRows())
                    .map(addedIngredientsTable::convertRowIndexToModel)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .forEach(
                            rowIndex -> {
                                AddedIngredient entityToDelete = addedIngredientsTableModel.getEntity(rowIndex);
                                entity.removeIngredient(entityToDelete);
                                addedIngredientsTableModel.deleteRow(rowIndex);
                            }
                    );
        }
    });

    public RecipeDialog(
            Recipe recipe,
            RecipeTableModel recipeTableModel,
            IngredientTableModel ingredientTableModel,
            CategoryTableModel categoryTableModel,
            CustomUnitTableModel unitTableModel
    ) {
        super(recipe, recipeTableModel.getEntities());
        setTwoPanels();
        ingredients = new JComboBox<>(ingredientTableModel.getArrayOfIngredients());

        List<Unit> unitList = new LinkedList<>();
        unitList.addAll(unitTableModel.getEntities());
        unitList.addAll(Arrays.stream(BaseUnit.values()).toList());
        units = new JComboBox<>(unitList.toArray(new Unit[0]));

        List<Category> categories = new LinkedList<>(categoryTableModel.getEntities());
        categories.add(0, null);
        categoryJComboBox.setModel(new DefaultComboBoxModel<>(categories.toArray(new Category[0])));

        removeIngredient.setEnabled(false);
        if (recipe != null) {
            setValues();
        } else {
            entity = new Recipe(null, null, null, 30, 0, "");
        }
        timeSpinner.setValue(entity.getPrepMinutes());

        categoryJComboBox.setSelectedItem(entity.getCategory());
        addedIngredientsTableModel = new AddedIngredientsTableModel(
                entity.getGuid(),
                MainWindow.commonDependencyProvider.getAddedIngredientCrudService()
        );
        addedIngredientsTable.setModel(addedIngredientsTableModel);
        addedIngredientsTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        addedIngredientsTable.setAutoCreateRowSorter(true);
        addFields();
    }

    private void rowSelectionChanged(ListSelectionEvent e) {
        removeIngredient.setEnabled(addedIngredientsTable.getSelectedRows().length > 0);
    }

    private void setValues() {
        recipeNameField.setText(entity.getName());
        categoryNameField.setText(entity.getCategoryName());
        categoryColor.setColor(entity.getCategory() == null
                ? new Color(0, 0, 0)
                : entity.getCategory().getColor());
        recipePortionsField.setModel(new SpinnerNumberModel(entity.getPortions(), 1, 200, 1));
        recipeDescriptionTextField.setLineWrap(true);
        recipeDescriptionTextField.setText(entity.getDescription());

    }

    private void addFields() {

        addLeft("Recipe Name:", recipeNameField);
        addLeft("Category:", categoryJComboBox);
        addLeft("Portions", recipePortionsField);
        addLeft("Preparation time in minutes: ", timeSpinner);
        addLeft(ingredients, amount, units, addIngredient, removeIngredient);
        addLeft(new JScrollPane(addedIngredientsTable), "span 5, grow");
        addRight("Description", new JScrollPane(recipeDescriptionTextField), "wmin 250lp, hmin 580lp, grow");
    }

    @Override
    Recipe getEntity() {
        Recipe setEntity = new Recipe();
        setEntity.setGuid(entity.getGuid());
        setEntity.setName(recipeNameField.getText());
        setEntity.setCategory((Category) categoryJComboBox.getSelectedItem());
        setEntity.setPortions((int) recipePortionsField.getValue());
        setEntity.setPrepMinutes((int) timeSpinner.getValue());
        setEntity.setDescription(recipeDescriptionTextField.getText());
        entity.getAddedIngredients().forEach(setEntity::addIngredient);
        return setEntity;
    }
}
