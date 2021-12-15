package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.data.count.CountMap1D;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCumulatedAnnualInterestImage;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCumulatedAnnualInterestImageHandler
        extends AbstractImageHandler<InCumulatedAnnualInterestImage>
        implements LoggingHelper {

    public AbstractCumulatedAnnualInterestImageHandler(
            ImageProcessor2 processor,
            InCumulatedAnnualInterestImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    public void init() throws Throwable {
    }

    protected CountMap1D<Integer> getYearInterestMapping(
            ConsumerAgent agent,
            Product product,
            List<Integer> years,
            DataAnalyser analyser) {
        CountMap1D<Integer> counter = new CountMap1D<>();
        for(int year: years) {
            double interest = analyser.getAnnualInterest(agent, product, year);
            if(agent.getProductInterest().isInterested(product, interest)) {
                counter.set(year, 1);
            } else {
                counter.set(year, 0);
            }
        }
        return counter;
    }

    protected CountMap1D<Integer> getAnnualCount(
            Product product,
            List<Integer> years,
            DataAnalyser analyser) {
        CountMap1D<Integer> cumulated = new CountMap1D<>();
        for(int year: years) {
            cumulated.init(year);
        }

        for(ConsumerAgent agent: getEnvironment().getAgents().iterableConsumerAgents()) {
            CountMap1D<Integer> cumulatedAgent = getYearInterestMapping(agent, product, years, analyser);
            cumulated.add(cumulatedAgent);
        }

        return cumulated;
    }

    protected JsonTableData3 createTableData(
            String yearLabel,
            String valueLabel) {
        Product product = processor.getUniqueProduct();
        List<Integer> years = processor.getAllSimulationYears();
        DataAnalyser analyser = getEnvironment().getDataAnalyser();

        CountMap1D<Integer> cumulated = getAnnualCount(product, years, analyser);

        JsonTableData3 tableData = new JsonTableData3();
        int rowCount = 0;
        //header
        tableData.setString(rowCount, 0, yearLabel);
        tableData.setString(rowCount, 1, valueLabel);
        //data
        for(int year: years) {
            rowCount++;
            tableData.setInt(rowCount, 0, year);
            tableData.setInt(rowCount, 1, cumulated.getCount(year));
        }

        return tableData;
    }
}
