package cz.muni.fi.pv168.project.business.service.export;



import cz.muni.fi.pv168.project.business.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.export.format.Format;

import cz.muni.fi.pv168.project.ui.action.mport.ImportType;

import java.util.Collection;

/**
 * Generic mechanism, allowing to import data from a file.
 */
public interface ImportService {

    /**
     * Imports data from a file.
     *
     * @param filePath absolute path of the export file (to be created or overwritten)
     *
     * @throws BatchOperationException if the import cannot be done
     */
    void importData(String filePath, ImportType importType);

    /**
     * Gets all available formats for import.
     */
    Collection<Format> getFormats();
}
