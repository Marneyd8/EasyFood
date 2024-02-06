package cz.muni.fi.pv168.project.business.model;

import java.awt.*;

public class Category extends Entity {
    private Color color;

    public Category() {
    }

    public Category(
            String guid,
            String categoryName,
            Color color
    ) {
        super(guid);
        this.name = categoryName;
        this.color = color;
    }

    public Category(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getColorCode() {
        return String.format("%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue());
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }
}
