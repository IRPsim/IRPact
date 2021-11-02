package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionZips;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedAnnualImage;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractComparedAnnualImageHandler
        extends AbstractImageHandler<InComparedAnnualImage>
        implements LoggingHelper {

    public AbstractComparedAnnualImageHandler(
            ImageProcessor2 processor,
            InComparedAnnualImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    public void init() throws Throwable {
        validate();
    }

    protected abstract void validate() throws Throwable;

    protected AnnualEnumeratedAdoptionZips getAdoptionData() {
        if(getGlobalData().contains(AnnualEnumeratedAdoptionZips.class)) {
            return getGlobalData().getAuto(AnnualEnumeratedAdoptionZips.class);
        } else {
            AnnualEnumeratedAdoptionZips data = new AnnualEnumeratedAdoptionZips();
            data.analyse(getEnvironment());
            getGlobalData().put(AnnualEnumeratedAdoptionZips.class, data);
            return data;
        }
    }

    protected JsonTableData3 createScaledAndUnscaledData(
            RealAdoptionData realData,
            ScaledRealAdoptionData scaledData,
            String simuLabel,
            String realScaledLabel,
            String realUnscaledSLabel,
            boolean showPreYear,
            boolean validZipsOnly) {
        List<Integer> years = processor.getAllSimulationYears();
        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
        Product product = processor.getSingletonProduct();
        AnnualEnumeratedAdoptionZips simuData = getAdoptionData();

        List<String> validZips;
        if(validZipsOnly) {
            validZips = scaledData.listValidZips(allZips);
            List<String> invalidZips = scaledData.listInvalidZips(allZips);
            List<String> unusedZips = scaledData.listUnusedZips(allZips);
            if(invalidZips.size() > 0) {
                warn("skip zips: invalid={}, unused={}", invalidZips, unusedZips);
            }
        } else {
            validZips = allZips;
        }

        JsonTableData3 data = new JsonTableData3();
        //header
        int rowIndex = 0;
        data.setString(rowIndex, 0, "years");
        data.setString(rowIndex, 1, simuLabel);
        data.setString(rowIndex, 2, realScaledLabel);
        data.setString(rowIndex, 3, realUnscaledSLabel);
        //initial - 1
        if(showPreYear) {
            rowIndex++;
            int yearBeforeStart = years.get(0) - 1;
            if(realData.hasYear(yearBeforeStart)) {
                data.setInt(rowIndex, 0, yearBeforeStart);
                double realUnscaled = realData.getUncumulated(yearBeforeStart, validZips);
                double realScaled = scaledData.getUncumulated(yearBeforeStart, validZips);
                data.setDouble(rowIndex, 1, realScaled); //simu == real
                data.setDouble(rowIndex, 2, realScaled);
                data.setDouble(rowIndex, 3, realUnscaled);
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            data.setInt(rowIndex, 0, y);
            double simu = simuData.getTotal(y, product, validZips);
            double realScaled = scaledData.getUncumulated(y, validZips);
            double realUnscaled = realData.getUncumulated(y, validZips);
            data.setDouble(rowIndex, 1, simu);
            data.setDouble(rowIndex, 2, realScaled);
            data.setDouble(rowIndex, 3, realUnscaled);
            rowIndex++;
        }

        return data;
    }

    protected JsonTableData3 createScaledData(
            ScaledRealAdoptionData scaledData,
            String simuLabel,
            String realLabel,
            boolean showPreYear,
            boolean validZipsOnly) {
        List<Integer> years = processor.getAllSimulationYears();
        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
        Product product = processor.getSingletonProduct();
        AnnualEnumeratedAdoptionZips simuData = getAdoptionData();

        List<String> validZips;
        if(validZipsOnly) {
            validZips = scaledData.listValidZips(allZips);
            List<String> invalidZips = scaledData.listInvalidZips(allZips);
            List<String> unusedZips = scaledData.listUnusedZips(allZips);
            if(invalidZips.size() > 0) {
                warn("skip zips: invalid={}, unused={}", invalidZips, unusedZips);
            }
        } else {
            validZips = allZips;
        }

        JsonTableData3 data = new JsonTableData3();
        //header
        int rowIndex = 0;
        data.setString(rowIndex, 0, "years");
        data.setString(rowIndex, 1, simuLabel);
        data.setString(rowIndex, 2, realLabel);
        //initial - 1
        if(showPreYear) {
            rowIndex++;
            int yearBeforeStart = years.get(0) - 1;
            if(scaledData.hasYear(yearBeforeStart)) {
                data.setInt(rowIndex, 0, yearBeforeStart);
                double realScaled = scaledData.getUncumulated(yearBeforeStart, validZips);
                data.setDouble(rowIndex, 1, realScaled); //simu == real
                data.setDouble(rowIndex, 2, realScaled);
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, scaledData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            data.setInt(rowIndex, 0, y);
            double simu = simuData.getTotal(y, product, validZips);
            double realScaled = scaledData.getUncumulated(y, validZips);
            data.setDouble(rowIndex, 1, simu);
            data.setDouble(rowIndex, 2, realScaled);
            rowIndex++;
        }

        return data;
    }

    protected JsonTableData3 createUnscaledData(
            RealAdoptionData realData,
            String simuLabel,
            String realLabel,
            boolean showPreYear,
            boolean validZipsOnly) {
        List<Integer> years = processor.getAllSimulationYears();
        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
        Product product = processor.getSingletonProduct();
        AnnualEnumeratedAdoptionZips simuData = getAdoptionData();

        List<String> validZips;
        if(validZipsOnly) {
            validZips = realData.listValidZips(allZips);
            List<String> invalidZips = realData.listInvalidZips(allZips);
            List<String> unusedZips = realData.listUnusedZips(allZips);
            if(invalidZips.size() > 0) {
                warn("skip zips: invalid={}, unused={}", invalidZips, unusedZips);
            }
        } else {
            validZips = allZips;
        }

        JsonTableData3 data = new JsonTableData3();
        //header
        int rowIndex = 0;
        data.setString(rowIndex, 0, "years");
        data.setString(rowIndex, 1, simuLabel);
        data.setString(rowIndex, 2, realLabel);
        //initial - 1
        if(showPreYear) {
            rowIndex++;
            int yearBeforeStart = years.get(0) - 1;
            if(realData.hasYear(yearBeforeStart)) {
                data.setInt(rowIndex, 0, yearBeforeStart);
                double realScaled = realData.getUncumulated(yearBeforeStart, validZips);
                data.setDouble(rowIndex, 1, realScaled); //simu == real
                data.setDouble(rowIndex, 2, realScaled);
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            data.setInt(rowIndex, 0, y);
            double simu = simuData.getTotal(y, product, validZips);
            double realScaled = realData.getUncumulated(y, validZips);
            data.setDouble(rowIndex, 1, simu);
            data.setDouble(rowIndex, 2, realScaled);
            rowIndex++;
        }

        return data;
    }
}
