package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.util.AdoptionPhase;
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
public class CumulativeAnnualPhaseWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulativeAnnualPhaseWithRscript.class);

    public CumulativeAnnualPhaseWithRscript(ImageProcessor imageProcessor) {
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
        return RScriptFactory.stackedBarChart0(
                getLocalizedString("title"),
                getLocalizedString("xarg"), getLocalizedString("yarg"), getLocalizedString("fillarg"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("sep"),
                new String[] {Element.NUMERIC, Element.CHARACTER, Element.NUMERIC},
                imageProcessor.getYearBreaksForPrettyR(),
                image.getImageWidth(), image.getImageHeight(),
                getLocalizedString("encoding")
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsPhase2 input = createAnnualAdoptionPhaseData();

        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("phase"),
                map("adoptionsCumulative")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsPhase2.getYear(entry);
            AdoptionPhase phase = AnnualAdoptionsPhase2.getPhase(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsPhase2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(print(phase)),
                    map(result.printCumulativeValue())
            ));
        }

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
