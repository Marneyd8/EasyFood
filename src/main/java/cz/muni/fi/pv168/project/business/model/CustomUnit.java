package cz.muni.fi.pv168.project.business.model;

public class CustomUnit extends Entity implements Unit {
    private String unitName;
    private String abbreviation;
    private double amount;
    private BaseUnit baseUnit;

    public double getAmount() {
        return amount;
    }

    public CustomUnit() {
    }

    public CustomUnit(
            String guid,
            String unitName,
            String abbreviation,
            double amount,
            BaseUnit baseUnit
    ) {
        super(guid);
        this.unitName = unitName;
        this.name = unitName;
        this.abbreviation = abbreviation;
        this.amount = amount;
        this.baseUnit = baseUnit;
    }

    public CustomUnit(String unitName, String abbreviation, double amount, BaseUnit baseUnit) {
        this.unitName = unitName;
        this.name = unitName;
        this.abbreviation = abbreviation;
        this.amount = amount;
        this.baseUnit = baseUnit;
    }

    @Override
    public String getName() {
        return unitName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getBaseAmountNumber() {
        return amount + "";
    }

    public String getBaseAmount() {
        return String.format("%.2f", amount) + " " + baseUnit.getAbbreviation();
    }

    public BaseUnit getBaseUnit() {
        return baseUnit;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.unitName = name;
    }
    @Override
    public boolean isCustom() { return true;}
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBaseUnit(BaseUnit baseUnit) {
        this.baseUnit = baseUnit;
    }

    @Override
    public String toString() {
        return unitName;
    }
}
