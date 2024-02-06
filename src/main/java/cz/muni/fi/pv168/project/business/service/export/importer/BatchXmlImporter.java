package cz.muni.fi.pv168.project.business.service.export.importer;

import cz.muni.fi.pv168.project.business.service.export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.export.format.Format;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

/**
 * @author Adam Juhas
 */
public class BatchXmlImporter implements BatchImporter {
    @Override
    public Batch importBatch(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ImporterHandler importerHandler = new ImporterHandler();
            saxParser.parse(filePath, importerHandler);
            return importerHandler.getBatch();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Format getFormat() {
        return new Format("XML", List.of("xml"));
    }
}
