package cz.muni.fi.pv168.project.business.model;

/**
 * Provider of globally unique identifiers for new entities.
 * <p>
 * The returned GUID should be globally unique. The provider will always
 * return a new identifier, which has not been used yet.
 */
public interface GuidProvider {
    String newGuid();
}
