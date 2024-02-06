package cz.muni.fi.pv168.project.business.model;

/**
 * @author Filip Skvara
 */
public interface Unit {
    String getName();
    String getAbbreviation();
    public String getGuid();
    public boolean isCustom();
}
