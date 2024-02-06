package cz.muni.fi.pv168.project.business.model;

/**
 * @author Filip Skvara
 */
public enum BaseUnit implements Unit {

    KILOGRAM("kg",0,"bu-kg"),
    GRAM("g",1,"bu-gg"),
    MILLILITER("ml",2,"bu-ml"),
    LITER("l",3,"bu-ll"),
    PIECE("pcs",4,"bu-pcs");
    private final String abbreviation;
    private final int index;
    private final String guid;

    BaseUnit(String abbreviation, int i, String guid) {
        index = i;
        this.guid = guid;
        this.abbreviation = abbreviation;
    }

    public static BaseUnit indexToUnit(int i) {
        return BaseUnit.values()[i];
    }
    public String getName() {
        return this.name();
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public boolean isCustom() {
        return false;
    }

    public int getIndex() {return index;}


    @Override
    public String toString() {
        return this.name();
    }
}
