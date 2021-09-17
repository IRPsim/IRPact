package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class AnnualZipWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualZipWithRscript.class);

    public AnnualZipWithRscript(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.ANNUAL_ZIP;
    }

    @Override
    protected RScriptBuilder getBuilder(InOutputImage image) {
        return RScriptFactory.lineChart0(
                getLocalizedString("title"),
                getLocalizedString("xarg"), getLocalizedString("yarg"), getLocalizedString("grparg"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("grplab"),
                getLocalizedString("sep"),
                new String[] {Element.NUMERIC, Element.CHARACTER, Element.NUMERIC},
                imageProcessor.getYearBreaksForPrettyR(),
                image.getLinewidthInt(),
                image.getImageWidth(), image.getImageHeight(),
                getLocalizedString("encoding")
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsZip2 input = createAnnualAdoptionZipData();

        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("zip"),
                map("adoptions")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsZip2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(result.printValue())
            ));
        }

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
