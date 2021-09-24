package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.DataAnalyser;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AnnualInterestWithGnuPlot2D extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualInterestWithGnuPlot2D.class);

    public AnnualInterestWithGnuPlot2D(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.ANNUAL_INTEREST_2D;
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        return GnuPlotFactory.stackedBarChart0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("sep"),
                imageProcessor.getDefaultBoxWidth(),
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

        DataAnalyser dataAnalyser = imageProcessor.getDataAnalyser();
        List<Integer> years = imageProcessor.getAllSimulationYears();
        List<Double> interestValues = imageProcessor.getInterestValues(product);

        JsonTableData3 tableData = new JsonTableData3();
        //header
        tableData.setString(0, 0, "year");
        int column = 1;
        for(double value: interestValues) {
            tableData.setDouble(0, column++, value);
        }
        //data
        int row = 1;
        for(int year: years) {
            tableData.setInt(row, 0, year);
            column = 1;
            for(double value: interestValues) {
                int interest = dataAnalyser.getCumulatedAnnualInterestCount(product, year, value);
                tableData.setInt(row, column++, interest);
            }
            row++;
        }

        return new CsvBasedImageData(getLocalizedString("sep"), tableData.toStringList());
    }
}
