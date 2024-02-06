package cz.muni.fi.pv168.project.business.service.export;


import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.service.export.batch.Batch;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.business.service.export.format.Format;
import cz.muni.fi.pv168.project.business.service.export.format.FormatMapping;


import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<CustomUnit> customUnitsCrudService;
    private final CrudService<Category> categoryCrudService;
    private final FormatMapping<BatchExporter> exporters;

    public GenericExportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Ingredient> ingredienbtCrudService,
            CrudService<CustomUnit> customUnitsCrudService,
            CrudService<Category> categoryCrudService,
            Collection<BatchExporter> exporters
    ) {
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredienbtCrudService;
        this.customUnitsCrudService = customUnitsCrudService;
        this.categoryCrudService = categoryCrudService;

        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = new Batch(recipeCrudService.findAll(), ingredientCrudService.findAll(),
                customUnitsCrudService.findAll(), categoryCrudService.findAll());
        exporter.exportBatch(batch, filePath);
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
