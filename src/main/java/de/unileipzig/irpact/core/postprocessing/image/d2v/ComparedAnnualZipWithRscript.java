package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.*;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZipWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZipWithRscript.class);

    public ComparedAnnualZipWithRscript(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.COMPARED_ANNUAL_ZIP;
    }

    @Override
    protected RScriptBuilder getBuilder(InOutputImage image) {
        return RScriptFactory.interactionLineChart0(
                getLocalizedString("title"),
                getLocalizedString("xarg"), getLocalizedString("yarg"), getLocalizedString("grparg"), getLocalizedString("distinctarg"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("grplab"), getLocalizedString("distinctlab"),
                getLocalizedString("distinct0lab"), getLocalizedString("distinct1lab"),
                getLocalizedString("sep"),
                new String[] {Element.NUMERIC, Element.CHARACTER, Element.NUMERIC, Element.CHARACTER},
                imageProcessor.getYearBreaksForPrettyR(),
                image.getLinewidthInt(),
                image.getImageWidth(), image.getImageHeight(),
                getLocalizedString("encoding")
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsZip2 input = createAnnualAdoptionZipData();

        RealAdoptionData realData = imageProcessor.getRealAdoptionData(image);

        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("zip"),
                map("adoptions"),
                map("real")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsZip2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(result.printValue()),
                    map(getLocalizedString("distinct0lab"))
            ));

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(Integer.toString(realData.get(year, zip))),
                    map(getLocalizedString("distinct1lab"))
            ));
        }

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
