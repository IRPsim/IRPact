package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.PostAnalysisData;
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

    protected String getNameForPhase(int phase) {
        switch (phase) {
            case PostAnalysisData.INITIAL_ADOPTED:
                return getLocalizedString("phase0");

            case PostAnalysisData.AWARENESS:
                return getLocalizedString("phase1");

            case PostAnalysisData.FEASIBILITY:
                return getLocalizedString("phase2");

            case PostAnalysisData.DECISION_MAKING:
                return getLocalizedString("phase3");

            case PostAnalysisData.ADOPTED:
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

        PostAnalysisData postData = imageProcessor.getEnvironment().getPostAnalysisData();
        List<Integer> years = imageProcessor.getAllSimulationYears();

        JsonTableData3 tableData = new JsonTableData3();
        //header
        tableData.setString(0, 0, "year");
        tableData.setString(0, 1, getNameForPhase(PostAnalysisData.INITIAL_ADOPTED));
        tableData.setString(0, 2, getNameForPhase(PostAnalysisData.AWARENESS));
        tableData.setString(0, 3, getNameForPhase(PostAnalysisData.FEASIBILITY));
        tableData.setString(0, 4, getNameForPhase(PostAnalysisData.DECISION_MAKING));
        tableData.setString(0, 5, getNameForPhase(PostAnalysisData.ADOPTED));
        //data
        int row = 1;
        for(int year: years) {
            Map<Integer, Integer> annualOverview = postData.getTransitionOverviewForYear(product, year);
            tableData.setInt(row, 0, year);
            tableData.setInt(row, 1, annualOverview.getOrDefault(PostAnalysisData.INITIAL_ADOPTED, 0));
            tableData.setInt(row, 2, annualOverview.getOrDefault(PostAnalysisData.AWARENESS, 0));
            tableData.setInt(row, 3, annualOverview.getOrDefault(PostAnalysisData.FEASIBILITY, 0));
            tableData.setInt(row, 4, annualOverview.getOrDefault(PostAnalysisData.DECISION_MAKING, 0));
            tableData.setInt(row, 5, annualOverview.getOrDefault(PostAnalysisData.ADOPTED, 0));
            row++;
        }

        return new CsvBasedImageData(getLocalizedString("sep"), tableData.toStringList());
    }
}
