package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionZips;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractComparedAnnualImageHandler<T extends InOutputImage2>
        extends AbstractImageHandler<T>
        implements LoggingHelper {

    public AbstractComparedAnnualImageHandler(
            ImageProcessor2 processor,
            T imageConfiguration) {
        super(processor, imageConfiguration);
    }

    public void init() throws Throwable {
        validate();
    }

    protected abstract void validate() throws Throwable;

    protected AnnualEnumeratedAdoptionZips getAdoptionData(boolean cumulated) {
        AnnualEnumeratedAdoptionZips data = getAdoptionData();
        if(cumulated && data.isNotCumulated()) {
            return (AnnualEnumeratedAdoptionZips) data.cumulate(processor.getAllSimulationYears());
        } else {
            return data;
        }
    }

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

//    protected JsonTableData3 createScaledAndUnscaledData(
//            RealAdoptionData realData,
//            ScaledRealAdoptionData scaledData,
//            String simuLabel,
//            String realScaledLabel,
//            String realUnscaledSLabel,
//            boolean showPreYear,
//            boolean validZipsOnly) {
//        List<Integer> years = processor.getAllSimulationYears();
//        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
//        Product product = processor.getUniqueProduct();
//        AnnualEnumeratedAdoptionZips simuData = getAdoptionData();
//
//        List<String> validZips;
//        if(validZipsOnly) {
//            validZips = scaledData.listValidZips(allZips);
//            List<String> invalidZips = scaledData.listInvalidZips(allZips);
//            List<String> unusedZips = scaledData.listUnusedZips(allZips);
//            if(invalidZips.size() > 0) {
//                warn("skip zips: invalid={}, unused={}", invalidZips, unusedZips);
//            }
//        } else {
//            validZips = allZips;
//        }
//
//        JsonTableData3 data = new JsonTableData3();
//        //header
//        int rowIndex = 0;
//        data.setString(rowIndex, 0, "years");
//        data.setString(rowIndex, 1, simuLabel);
//        data.setString(rowIndex, 2, realScaledLabel);
//        data.setString(rowIndex, 3, realUnscaledSLabel);
//        //initial - 1
//        if(showPreYear) {
//            rowIndex++;
//            int yearBeforeStart = processor.getPreFirstSimulationYear();
//            if(realData.hasYear(yearBeforeStart)) {
//                data.setInt(rowIndex, 0, yearBeforeStart);
//                double realUnscaled = realData.getCumulated(yearBeforeStart, validZips);
//                double realScaled = scaledData.getCumulated(yearBeforeStart, validZips);
//                data.setDouble(rowIndex, 1, realScaled); //simu == real
//                data.setDouble(rowIndex, 2, realScaled);
//                data.setDouble(rowIndex, 3, realUnscaled);
//            } else {
//                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
//            }
//        }
//        //data
//        rowIndex++;
//        for(int y: years) {
//            data.setInt(rowIndex, 0, y);
//            double simu = simuData.getCount(y, product, validZips);
//            double realScaled = scaledData.getUncumulated(y, validZips);
//            double realUnscaled = realData.getUncumulated(y, validZips);
//            data.setDouble(rowIndex, 1, simu);
//            data.setDouble(rowIndex, 2, realScaled);
//            data.setDouble(rowIndex, 3, realUnscaled);
//            rowIndex++;
//        }
//
//        return data;
//    }

    protected JsonTableData3 createScaledData(
            ScaledRealAdoptionData scaledData,
            String simuLabel,
            String realLabel,
            boolean showPreYear,
            boolean validZipsOnly,
            boolean cumulated) {
        List<Integer> years = processor.getAllSimulationYears();
        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
        Product product = processor.getUniqueProduct();
        AnnualEnumeratedAdoptionZips simuData = getAdoptionData(cumulated);

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
            int yearBeforeStart = processor.getPreFirstSimulationYear();
            if(scaledData.hasYear(yearBeforeStart)) {
                data.setInt(rowIndex, 0, yearBeforeStart);
                double realScaled = scaledData.get(true, yearBeforeStart, validZips);
                data.setDouble(rowIndex, 1, realScaled); //simu == real
                data.setDouble(rowIndex, 2, realScaled);

                info("[createScaledData]");
                info("[{}] {} initial-simu: {}", getResourceKey(), yearBeforeStart, simuData.getInitialCount(product, validZips));
                info("[{}] {} initial-real: {}", getResourceKey(), yearBeforeStart, realScaled);
                info("[{}] {} initial-real2: {}", getResourceKey(), yearBeforeStart, scaledData.get(cumulated, yearBeforeStart, scaledData.getAllZips()));
                info("[{}] {} initial-real3: {}", getResourceKey(), yearBeforeStart, scaledData.getCumulated(yearBeforeStart));
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, scaledData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            data.setInt(rowIndex, 0, y);
            double simu = simuData.getCount(y, product, validZips);
            double realScaled = scaledData.get(cumulated, y, validZips);
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
            boolean validZipsOnly,
            boolean cumulated) {
        List<Integer> years = processor.getAllSimulationYears();
        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
        Product product = processor.getUniqueProduct();
        AnnualEnumeratedAdoptionZips simuData = getAdoptionData(cumulated);

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
            int yearBeforeStart = processor.getPreFirstSimulationYear();
            if(realData.hasYear(yearBeforeStart)) {
                data.setInt(rowIndex, 0, yearBeforeStart);
                double realScaled = realData.get(true, yearBeforeStart, validZips);
                data.setDouble(rowIndex, 1, realScaled); //simu == real
                data.setDouble(rowIndex, 2, realScaled);

                info("[createUnscaledData]");
                info("[{}] {} initial-simu: {}", getResourceKey(), yearBeforeStart, simuData.getInitialCount(product, validZips));
                info("[{}] {} initial-real: {}", getResourceKey(), yearBeforeStart, realScaled);
                info("[{}] {} initial-real2: {}", getResourceKey(), yearBeforeStart, realData.get(cumulated, yearBeforeStart, realData.getAllZips()));
                info("[{}] {} initial-real3: {}", getResourceKey(), yearBeforeStart, realData.getCumulated(yearBeforeStart));
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            data.setInt(rowIndex, 0, y);
            double simu = simuData.getCount(y, product, validZips);
            double realScaled = realData.get(cumulated, y, validZips);
            data.setDouble(rowIndex, 1, simu);
            data.setDouble(rowIndex, 2, realScaled);
            rowIndex++;
        }

        return data;
    }
}
