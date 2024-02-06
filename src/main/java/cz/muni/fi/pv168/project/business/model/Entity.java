package cz.muni.fi.pv168.project.business.model;

import java.util.Objects;

public abstract class Entity {

    protected String guid;

    protected String name;


    protected Entity(String guid) {
        this.guid = guid;
    }

    protected Entity() {
    }

    /**
     * Returns globally unique identifier of the given entity.
     */
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(guid, entity.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid);
    }
}
