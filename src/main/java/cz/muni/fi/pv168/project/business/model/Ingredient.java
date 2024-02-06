package cz.muni.fi.pv168.project.business.model;

public class Ingredient extends Entity {
    private int nutritionalValue;

    public Ingredient() {
    }

    public Ingredient(
            String guid,
            String name,
            int nutritionalValue
    ) {
        super(guid);
        this.name = name;
        this.nutritionalValue = nutritionalValue;
    }

    public Ingredient(String name, int nutritionalValue) {
        this.name = name;
        this.nutritionalValue = nutritionalValue;
    }

    public void setNutritionalValue(int nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }


    public int getNutritionalValue() {
        return nutritionalValue;
    }

    @Override
    public String toString() {
        return name;
    }
}
