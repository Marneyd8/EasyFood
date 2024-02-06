package cz.muni.fi.pv168.project.ui.model;


import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import java.util.List;

public class CustomUnitTableModel extends AbstractEntityTableModel<CustomUnit> {
    private final CrudService<CustomUnit> customUnitCrudService;

    public CustomUnitTableModel(CrudService<CustomUnit> customUnitCrudService) {
        super(List.of(
                Column.readonly("Name", String.class, CustomUnit::getName),
                Column.readonly("Abbreviation", String.class, CustomUnit::getAbbreviation),
                Column.readonly("BaseAmount", String.class, CustomUnit::getBaseAmount)
        ), customUnitCrudService.findAll(), customUnitCrudService);
        this.customUnitCrudService = customUnitCrudService;
    }

}
