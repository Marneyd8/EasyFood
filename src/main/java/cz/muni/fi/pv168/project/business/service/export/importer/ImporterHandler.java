package cz.muni.fi.pv168.project.business.service.export.importer;

import cz.muni.fi.pv168.project.business.model.*;
import cz.muni.fi.pv168.project.business.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.business.service.export.batch.Batch;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

public class ImporterHandler extends DefaultHandler {
    private final List<Category> categoryList;
    private final List<CustomUnit> unitList;
    private final List<Ingredient> ingredientList;
    private final List<Recipe> recipeList;

    public static final String CATEGORY = "Category";
    public static final String UNIT = "Unit";
    public static final String INGREDIENT = "Ingredient";
    public static final String RECIPE = "Recipe";
    public static final String ADDED_INGREDIENTS = "AddedIngredients";
    public static final String ADDED_INGREDIENT = "AddedIngredient";
    private StringBuilder elementValue;
    private Category activeCategory;
    private CustomUnit activeUnit;
    private Ingredient activeIngredient;
    private Recipe activeRecipe;
    private AddedIngredient activeAddedIngredient;

    private List<AddedIngredient> addedIngredients;

    public ImporterHandler() {
        this.categoryList = new ArrayList<>();
        this.unitList = new ArrayList<>();
        this.ingredientList = new ArrayList<>();
        this.recipeList = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case CATEGORY -> activeCategory = new Category();
            case UNIT -> activeUnit = new CustomUnit();
            case INGREDIENT -> activeIngredient = new Ingredient();
            case RECIPE -> activeRecipe = new Recipe();
            case ADDED_INGREDIENTS -> addedIngredients = new ArrayList<>();
            case ADDED_INGREDIENT -> activeAddedIngredient = new AddedIngredient();
            default -> elementValue = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case CATEGORY -> categoryList.add(activeCategory);
            case UNIT -> unitList.add(activeUnit);
            case INGREDIENT -> ingredientList.add(activeIngredient);
            case RECIPE -> recipeList.add(activeRecipe);

            case "CategoryName" -> activeCategory.setName(elementValue.toString());
            case "Color" -> activeCategory.setColor(new Color(Integer.parseInt(elementValue.toString())));

            case "UnitName" -> activeUnit.setName(elementValue.toString());
            case "Abbreviation" -> activeUnit.setAbbreviation(elementValue.toString());
            case "Amount" -> activeUnit.setAmount(Double.parseDouble(elementValue.toString()));
            case "BaseUnitName" -> activeUnit.setBaseUnit(List.of(BaseUnit.values()).stream()
                    .filter(bu -> bu.getName().contentEquals(elementValue))
                    .findFirst()
                    .orElseThrow(() -> new DataManipulationException(
                            "BaseUnit with name: " + elementValue.toString() + " does not exist")
                    ));

            case "IngredientName" -> activeIngredient.setName(elementValue.toString());
            case "NutritionalValue" -> activeIngredient.setNutritionalValue(Integer.parseInt(elementValue.toString()));

            case "RecipeName" -> activeRecipe.setName(elementValue.toString());
            case "PrepMinutes" -> activeRecipe.setPrepMinutes(Integer.parseInt(elementValue.toString()));
            case "Portions" -> activeRecipe.setPortions(Integer.parseInt(elementValue.toString()));
            case "RecipeCategoryName" -> activeRecipe.setCategory(parseCategory(elementValue.toString()));
            case "Description" -> activeRecipe.setDescription(elementValue.toString());
            case "AiIngredientName" -> activeAddedIngredient.setIngredient(parseIngredient(elementValue.toString()));
            case "AiRecipeName" -> activeAddedIngredient.setRecipe(activeRecipe);
            case "AiUnitName" -> activeAddedIngredient.setUnit(parseUnit(elementValue.toString()));
            case "Quantity" -> activeAddedIngredient.setQuantity(Double.parseDouble(elementValue.toString()));
            case ADDED_INGREDIENT -> assignAddedIngredient(activeRecipe, activeAddedIngredient);

        }
    }

    private void assignAddedIngredient(Recipe recipe, AddedIngredient addedIngredient) {
        recipe.getAddedIngredients().add(addedIngredient);
        addedIngredients.add(addedIngredient);
    }

    private Category parseCategory(String categoryName) {
        Optional<Category> optionalCategory = categoryList.stream()
                .filter(category -> category.getName().equals(categoryName))
                .findFirst();
        if (optionalCategory.isEmpty()) {
            popUpDialog(new DataManipulationException("Category with name: " + categoryName + " not found!"));
        }
        return optionalCategory.get();

    }

    private Ingredient parseIngredient(String ingredientName) {
        Optional<Ingredient> optionalIngredient = ingredientList.stream()
                .filter(ingredient -> ingredient.getName().equals(ingredientName))
                .findFirst();
        if (optionalIngredient.isEmpty()) {
            popUpDialog(new DataManipulationException("Ingredient with name: " + ingredientName + " not found!"));
        }
        return optionalIngredient.get();
    }

    private PreparationTime parsePreparationTime(String prepTime) {
        String[] split = prepTime.split(" ");
        return new PreparationTime(Integer.parseInt(split[0]), Integer.parseInt(split[2]));
    }

    private Unit parseUnit(String unitName) {
        List<Unit> units = new ArrayList<>(unitList);
        units.addAll(Arrays.stream(BaseUnit.values()).toList());
        Optional<Unit> optionalUnit = units.stream()
                .filter(unit -> unit.getName().equals(unitName))
                .findFirst();
        if (optionalUnit.isEmpty()) {
            popUpDialog(new DataManipulationException("Unit with name: " + unitName + " not found!"));
        }
        return optionalUnit.get();
    }

    public Batch getBatch() {
        return new Batch(recipeList, ingredientList, unitList, categoryList);
    }

    private void popUpDialog(DataManipulationException e) {
        JOptionPane.showMessageDialog(new JPanel(), e.getMessage());
        throw e;
    }

}
