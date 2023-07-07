package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
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
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class AnnualPhaseOverviewWithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualPhaseOverviewWithGnuPlot.class);

    public AnnualPhaseOverviewWithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.ANNUAL_PHASE_OVERVIEW;
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

    protected String getNameForPhase(DataAnalyser.Phase phase) {
        switch (phase) {
            case INITIAL_ADOPTED:
                return getLocalizedString("phase0");

            case AWARENESS:
                return getLocalizedString("phase1");

            case FEASIBILITY:
                return getLocalizedString("phase2");

            case DECISION_MAKING:
                return getLocalizedString("phase3");

            case ADOPTED:
                return getLocalizedString("phase4");

            default:
                throw new IllegalArgumentException("unsupported phase:" + phase);
        }
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

        JsonTableData3 tableData = new JsonTableData3();
        //header
        tableData.setString(0, 0, "year");
        tableData.setString(0, 1, getNameForPhase(DataAnalyser.Phase.INITIAL_ADOPTED));
        tableData.setString(0, 2, getNameForPhase(DataAnalyser.Phase.AWARENESS));
        tableData.setString(0, 3, getNameForPhase(DataAnalyser.Phase.FEASIBILITY));
        tableData.setString(0, 4, getNameForPhase(DataAnalyser.Phase.DECISION_MAKING));
        tableData.setString(0, 5, getNameForPhase(DataAnalyser.Phase.ADOPTED));
        //data
        int row = 1;
        for(int year: years) {
            Map<DataAnalyser.Phase, Integer> annualOverview = dataAnalyser.getTransitionOverviewForYear(product, year);
            tableData.setInt(row, 0, year);
            tableData.setInt(row, 1, annualOverview.getOrDefault(DataAnalyser.Phase.INITIAL_ADOPTED, 0));
            tableData.setInt(row, 2, annualOverview.getOrDefault(DataAnalyser.Phase.AWARENESS, 0));
            tableData.setInt(row, 3, annualOverview.getOrDefault(DataAnalyser.Phase.FEASIBILITY, 0));
            tableData.setInt(row, 4, annualOverview.getOrDefault(DataAnalyser.Phase.DECISION_MAKING, 0));
            tableData.setInt(row, 5, annualOverview.getOrDefault(DataAnalyser.Phase.ADOPTED, 0));
            row++;
        }

        return new CsvBasedImageData(getLocalizedString("sep"), tableData.toStringList());
    }
}
