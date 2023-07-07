package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InInterestOverviewImage;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractInterestOverviewImageHandler
        extends AbstractImageHandler<InInterestOverviewImage>
        implements LoggingHelper {

    public AbstractInterestOverviewImageHandler(
            ImageProcessor2 processor,
            InInterestOverviewImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    public void init() throws Throwable {
    }

    protected JsonTableData3 createTableData() {
        Product product = processor.getUniqueProduct();
        List<Integer> years = processor.getAllSimulationYears();
        List<Double> interestValues = processor.getInterestValues(product);
        DataAnalyser analyser = getEnvironment().getDataAnalyser();

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
                int interest = analyser.getCumulatedAnnualInterestCount(product, year, value);
                tableData.setInt(row, column++, interest);
            }
            row++;
        }

        return tableData;
    }
}
