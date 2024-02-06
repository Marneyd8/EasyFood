package cz.muni.fi.pv168.project.ui.action.mport;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.service.crud.*;
import cz.muni.fi.pv168.project.business.service.export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.export.GenericImportService;
import cz.muni.fi.pv168.project.business.service.export.ParallelImport;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchXmlExporter;
import cz.muni.fi.pv168.project.business.service.export.importer.BatchXmlImporter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class ImportAction extends AbstractAction {

    protected final CrudService<Category> categoryCrudService;
    protected final CrudService<CustomUnit> unitService;
    protected final CrudService<Ingredient> ingredientCrudService;
    protected final CrudService<Recipe> recipeCrudService;
    private final Runnable callback;

    private final ImportType importType;

    public ImportAction(
            String name,
            CrudService<Category> categoryCrudService,
            CrudService<CustomUnit> unitService,
            CrudService<Ingredient> ingredientCrudService,
            CrudService<Recipe> recipeCrudService,
            Runnable callback,
            ImportType importType
    ) {
        super(name);
        this.categoryCrudService = categoryCrudService;
        this.unitService = unitService;
        this.ingredientCrudService = ingredientCrudService;
        this.recipeCrudService = recipeCrudService;
        this.callback = callback;
        this.importType = importType;

        if (importType == ImportType.OVERWRITE) {
            putValue(SHORT_DESCRIPTION, "Overwrites all data from an XML file");
            putValue(MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
        } else {
            putValue(SHORT_DESCRIPTION, "Appends all data from an XML file");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("xml file", "xml"));
        fileChooser.getChoosableFileFilters();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ParallelImport importer = new ParallelImport(selectedFile, callback, importType);
            importer.execute();
            /*BatchXmlImporter xmlImporter = new BatchXmlImporter();
            GenericImportService genericImportService = new GenericImportService(
                    recipeCrudService,
                    ingredientCrudService,
                    unitService,
                    categoryCrudService,
                    List.of(xmlImporter),
                    new GenericExportService(
                            recipeCrudService,
                            ingredientCrudService,
                            unitService,
                            categoryCrudService,
                            List.of(new BatchXmlExporter())
                    )
            );
            genericImportService.importData(selectedFile.getAbsolutePath(), importType);
            callback.run();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            JOptionPane.showMessageDialog(null, "Import has successfully finished.");*/
        }
    }
}
