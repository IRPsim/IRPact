package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionTotal;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Deprecated
public class ComparedAnnualWithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualWithGnuPlot.class);

    public ComparedAnnualWithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.COMPARED_ANNUAL;
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        return GnuPlotFactory.interactionLineChartForTwoValues0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("sep"),
                image.getLinewidthInt(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        List<Product> products = imageProcessor.getAllProducts();
        if(products.size() != 1) {
            throw new IllegalStateException("requires exactly one product");
        }
        Product product = products.get(0);

        RealAdoptionData realData = imageProcessor.getRealAdoptionData(image);
        Set<String> validZips = imageProcessor.getValidZips(realData, RAConstants.ZIP);
        Set<String> invalidZips = imageProcessor.getInvalidZips(realData, RAConstants.ZIP);

        trace("valid zips: {}", validZips);
        warn("invalid zips: {}", invalidZips);

        AnnualEnumeratedAdoptionTotal simuData = createAnnualEnumeratedAdoptionTotal((ca, ap) -> {
            String caZip = ca.findAttribute(RAConstants.ZIP)
                    .asValueAttribute()
                    .getStringValue();
            return validZips.contains(caZip);
        });

        List<Integer> years = imageProcessor.getAllSimulationYears();

        List<List<String>> csvData = new ArrayList<>();
        //header
        csvData.add(Arrays.asList(
                "years",
                getLocalizedString("simulab"),
                getLocalizedString("reallab")
        ));
        //data
        for(int year: years) {
            double real = realData.getUncumulated(year, validZips);
            double simu = simuData.getCount(year, product);

            csvData.add(Arrays.asList(
                    Integer.toString(year),
                    Double.toString(simu),
                    Double.toString(real)
            ));
        }

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
