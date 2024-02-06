package cz.muni.fi.pv168.project.data;


import cz.muni.fi.pv168.project.business.model.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

public final class TestDataGenerator {
    private final GuidProvider uuidProvider = new UuidGuidProvider();
    private static final List<String> RECIPE_NAMES = List.of("Smažák", "Pizza", "Vývar", "Guláš", "Mýchané vajíčka",
            "Hranolky", "Řízek", "Americké brambory", "Pečené kuře", "Grilled cheese");

    private static final List<String> INGREDIENT_NAMES = List.of("Paprika", "Brambory", "Máslo", "Rýže", "Vejce",
            "Hovězí maso", "Kuřecí maso", "Eidam", "Mouka", "Špagety", "Bílý jogurt", "Fazole", "Sůl", "Cukr", "Pepř", "Kari");

    public static final List<String> NATIONAL_CATEGORIES = List.of("Italian", "French", "Chinese",
            "Mexican", "Japanese", "Indian", "Thai", "Greek", "Spanish",
            "Middle Eastern");

    public static final List<String> FOOD_CATEGORIES = List.of("Appetizers", "Pizza", "Soup", "Stew", "Egg Dishes",
            "Side Dishes", "Potato Dishes", "Chicken", "Sandwiches", "Vegan", "Non-vegan", "Main Course", "Lunch", "Desert");

    private static final List<String> CUSTOM_UNIT_NAMES = new ArrayList<>();
    private static final List<String> CUSTOM_UNIT_ABBREVIATIONS = new ArrayList<>();
    private final List<Category> categories;
    private final List<CustomUnit> units;
    private final List<Ingredient> ingredients;
    private final List<Recipe> recipes;

    public List<Category> getCategories() {
        return categories;
    }

    public List<CustomUnit> getCustomUnits() {
        return units;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }


    static {
        CUSTOM_UNIT_NAMES.add("pinch");
        CUSTOM_UNIT_ABBREVIATIONS.add("pn");
        CUSTOM_UNIT_NAMES.add("slice");
        CUSTOM_UNIT_ABBREVIATIONS.add("sl");
        CUSTOM_UNIT_NAMES.add("handful");
        CUSTOM_UNIT_ABBREVIATIONS.add("hd");
        CUSTOM_UNIT_NAMES.add("sprig");
        CUSTOM_UNIT_ABBREVIATIONS.add("spg");
        CUSTOM_UNIT_NAMES.add("strip");
        CUSTOM_UNIT_ABBREVIATIONS.add("str");
        CUSTOM_UNIT_NAMES.add("slab");
        CUSTOM_UNIT_ABBREVIATIONS.add("slb");
        CUSTOM_UNIT_NAMES.add("bunch");
        CUSTOM_UNIT_ABBREVIATIONS.add("bnch");
        CUSTOM_UNIT_NAMES.add("packet");
        CUSTOM_UNIT_ABBREVIATIONS.add("pkt");
        // Add more custom units as needed
    }

    //private final BaseUnits baseUnits = new BaseUnits();


    public TestDataGenerator() {
        this.categories = createTestCategories();
        this.units = createTestCustomUnits(5);
        this.ingredients = createTestIngredients(10);
        this.recipes = createTestRecipes(10);
    }


//    private static final LocalDate MIN_BIRTH_DATE = LocalDate.of(1950, JANUARY, 1);
//    private static final LocalDate MAX_BIRTH_DATE = LocalDate.of(2002, DECEMBER, 31);

    private final Random random = new Random(2L);

    public List<Category> createTestCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            categories.add(createTestCategory());
        }
        return categories;
    }

    private List<Recipe> createTestRecipes(int count) {
        return Stream
                .generate(this::createTestRecipe)
                .limit(count)
                .collect(Collectors.toList());
    }


    public List<Ingredient> createTestIngredients(int count) {
        return Stream
                .generate(this::createTestIngredient)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<CustomUnit> createTestCustomUnits(int count) {
        return Stream
                .generate(this::createTestCustomUnit)
                .limit(count)
                .collect(Collectors.toList());

    }

    private CustomUnit createTestCustomUnit() {
        int position = random.nextInt(CUSTOM_UNIT_NAMES.size());
        String customUnitName = CUSTOM_UNIT_NAMES.get(position);
        String customUnitAbbreviation = CUSTOM_UNIT_ABBREVIATIONS.get(position);
        double amount = random.nextDouble() * 100;
        BaseUnit baseUnit = selectRandom(List.of(BaseUnit.values()));
        CustomUnit unit = new CustomUnit(customUnitName, customUnitAbbreviation, amount, baseUnit);
        unit.setGuid(uuidProvider.newGuid());
        return unit;
    }

    //    String guid,
//    String recipeName,
//    Category category,
//    PreparationTime preparationTime,
//    int nutritionalValue,
//    int portions,
//    String description
    public Recipe createTestRecipe() {

        String recipeName = selectRandom(RECIPE_NAMES);
        int portions = random.nextInt(10);
        Recipe recipe = new Recipe(
                uuidProvider.newGuid(),
                recipeName,
                categories.get(random.nextInt(categories.size())),
                20,
                portions,
                "desc"
        );

        recipe.setGuid(uuidProvider.newGuid());
        recipe.setCategory(categories.get(random.nextInt(categories.size())));
        return recipe;
    }

    private Ingredient createTestIngredient() {
        String ingredientName = selectRandom(INGREDIENT_NAMES);
        int nutritionalValue = random.nextInt(10000);
        Ingredient ingredient = new Ingredient(ingredientName, nutritionalValue);
        ingredient.setGuid(uuidProvider.newGuid());
        return ingredient;
    }

    private Category createTestCategory() {
        List<String> ALL_CATEGORIES = new ArrayList<>();
        ALL_CATEGORIES.addAll(NATIONAL_CATEGORIES);
        ALL_CATEGORIES.addAll(FOOD_CATEGORIES);
        String categoryName = selectRandom(ALL_CATEGORIES);
        Color categoryColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Category category = new Category(categoryName, categoryColor);
        category.setGuid(uuidProvider.newGuid());
        return category;
    }


    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }

    private LocalDate selectRandomLocalDate(LocalDate min, LocalDate max) {
        int maxDays = Math.toIntExact(DAYS.between(min, max) + 1);
        int days = random.nextInt(maxDays);
        return min.plusDays(days);
    }

}
