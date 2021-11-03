package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InProcessPhaseOverviewImage;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractProcessPhaseOverviewImageHandler
        extends AbstractQuantilRangeImageHandler<InProcessPhaseOverviewImage>
        implements LoggingHelper {

    public AbstractProcessPhaseOverviewImageHandler(
            ImageProcessor2 processor,
            InProcessPhaseOverviewImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    protected String getNameForPhase(DataAnalyser.Phase phase) {
        switch (phase) {
            case INITIAL_ADOPTED:
                return getLocalizedString("initialAdopted");

            case AWARENESS:
                return getLocalizedString("awareness");

            case FEASIBILITY:
                return getLocalizedString("feasibility");

            case DECISION_MAKING:
                return getLocalizedString("decisionMaking");

            case ADOPTED:
                return getLocalizedString("adopted");

            default:
                throw new IllegalArgumentException("unsupported phase:" + phase);
        }
    }

    protected JsonTableData3 createTableData() {
        Product product = processor.getSingletonProduct();
        List<Integer> years = processor.getAllSimulationYears();

        DataAnalyser analyser = getEnvironment().getDataAnalyser();

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
            Map<DataAnalyser.Phase, Integer> annualOverview = analyser.getTransitionOverviewForYear(product, year);
            tableData.setInt(row, 0, year);
            tableData.setInt(row, 1, annualOverview.getOrDefault(DataAnalyser.Phase.INITIAL_ADOPTED, 0));
            tableData.setInt(row, 2, annualOverview.getOrDefault(DataAnalyser.Phase.AWARENESS, 0));
            tableData.setInt(row, 3, annualOverview.getOrDefault(DataAnalyser.Phase.FEASIBILITY, 0));
            tableData.setInt(row, 4, annualOverview.getOrDefault(DataAnalyser.Phase.DECISION_MAKING, 0));
            tableData.setInt(row, 5, annualOverview.getOrDefault(DataAnalyser.Phase.ADOPTED, 0));
            row++;
        }

        return tableData;
    }
}
