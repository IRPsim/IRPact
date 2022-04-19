package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.data.MutableInt;
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
public abstract class AbstractComparedAnnualZipImageHandler<T extends InOutputImage2>
        extends AbstractImageHandler<T>
        implements LoggingHelper {

    public AbstractComparedAnnualZipImageHandler(
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
//            String simuSuffix,
//            String realScaledSuffix,
//            String realUnscaledSuffix,
//            boolean showPreYear,
//            boolean validZipsOnly,
//            MutableInt zipCounter) {
//        List<Integer> years = processor.getAllSimulationYears();
//        List<String> allZips = processor.getAllZips(RAConstants.ZIP);
//        Product product = processor.getSingletonProduct();
//        AnnualEnumeratedAdoptionZips simuData = getAdoptionData();
//
//        List<String> validZips;
//        if(validZipsOnly) {
//            validZips = realData.listValidZips(allZips);
//            List<String> invalidZips = realData.listInvalidZips(allZips);
//            List<String> unusedZips = realData.listUnusedZips(allZips);
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
//        int columnIndex = 0;
//        data.setString(rowIndex, columnIndex++, "years");
//        for(String zip: validZips) {
//            data.setString(rowIndex, columnIndex++, zip + simuSuffix);
//            data.setString(rowIndex, columnIndex++, zip + realScaledSuffix);
//            data.setString(rowIndex, columnIndex++, zip + realUnscaledSuffix);
//        }
//        //initial - 1
//        if(showPreYear) {
//            rowIndex++;
//            int yearBeforeStart = years.get(0) - 1;
//            if(realData.hasYear(yearBeforeStart)) {
//                columnIndex = 0;
//                data.setInt(rowIndex, columnIndex++, yearBeforeStart);
//                for(String zip: validZips) {
//                    double realScaled = scaledData.hasZip(zip) ? scaledData.getUncumulated(yearBeforeStart, zip) : 0;
//                    double realUnscaled = realData.hasZip(zip) ? realData.getUncumulated(yearBeforeStart, zip) : 0;
//                    data.setDouble(rowIndex, columnIndex++, realScaled); //simu == real5
//                    data.setDouble(rowIndex, columnIndex++, realScaled);
//                    data.setDouble(rowIndex, columnIndex++, realUnscaled);
//                }
//            } else {
//                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
//            }
//        }
//        //data
//        rowIndex++;
//        for(int y: years) {
//            columnIndex = 0;
//            data.setInt(rowIndex, columnIndex++, y);
//            for(String zip: validZips) {
//                double simu = simuData.getCount(y, product, zip);
//                double realScaled = scaledData.hasZip(zip) ? scaledData.getUncumulated(y, zip) : 0;
//                double realUnscaled = realData.hasZip(zip) ? realData.getUncumulated(y, zip) : 0;
//                data.setDouble(rowIndex, columnIndex++, simu);
//                data.setDouble(rowIndex, columnIndex++, realScaled);
//                data.setDouble(rowIndex, columnIndex++, realUnscaled);
//            }
//            rowIndex++;
//        }
//
//        zipCounter.set(validZips.size());
//        return data;
//    }

    protected JsonTableData3 createScaledData(
            ScaledRealAdoptionData scaledData,
            String simuSuffix,
            String realSuffix,
            boolean showPreYear,
            boolean validZipsOnly,
            boolean cumulated,
            MutableInt zipCounter) {
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
        int columnIndex = 0;
        data.setString(rowIndex, columnIndex++, "years");
        for(String zip: validZips) {
            data.setString(rowIndex, columnIndex++, zip + simuSuffix);
            data.setString(rowIndex, columnIndex++, zip + realSuffix);
        }
        //initial - 1
        if(showPreYear) {
            rowIndex++;
            int yearBeforeStart = years.get(0) - 1;
            if(scaledData.hasYear(yearBeforeStart)) {
                columnIndex = 0;
                data.setInt(rowIndex, columnIndex++, yearBeforeStart);
                for(String zip: validZips) {
                    double realScaled = scaledData.hasZip(zip) ? scaledData.get(cumulated, yearBeforeStart, zip) : 0;
                    data.setDouble(rowIndex, columnIndex++, realScaled); //simu == real
                    data.setDouble(rowIndex, columnIndex++, realScaled);
                }
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, scaledData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            columnIndex = 0;
            data.setInt(rowIndex, columnIndex++, y);
            for(String zip: validZips) {
                double simu = simuData.getCount(y, product, zip);
                double realScaled = scaledData.hasZip(zip) ? scaledData.get(cumulated, y, zip) : 0;
                data.setDouble(rowIndex, columnIndex++, simu);
                data.setDouble(rowIndex, columnIndex++, realScaled);
            }
            rowIndex++;
        }

        zipCounter.set(validZips.size());
        return data;
    }

    protected JsonTableData3 createUnscaledData(
            RealAdoptionData realData,
            String simuSuffix,
            String realSuffix,
            boolean showPreYear,
            boolean validZipsOnly,
            boolean cumulated,
            MutableInt zipCounter) {
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
        int columnIndex = 0;
        data.setString(rowIndex, columnIndex++, "years");
        for(String zip: validZips) {
            data.setString(rowIndex, columnIndex++, zip + simuSuffix);
            data.setString(rowIndex, columnIndex++, zip + realSuffix);
        }
        //initial - 1
        if(showPreYear) {
            rowIndex++;
            int yearBeforeStart = years.get(0) - 1;
            if(realData.hasYear(yearBeforeStart)) {
                columnIndex = 0;
                data.setInt(rowIndex, columnIndex++, yearBeforeStart);
                for(String zip: validZips) {
                    double real = realData.hasZip(zip) ? realData.get(cumulated, yearBeforeStart, zip) : 0;
                    data.setDouble(rowIndex, columnIndex++, real); //simu == real
                    data.setDouble(rowIndex, columnIndex++, real);
                }
            } else {
                info("missing data, skip 'year before start' (year={}, available={}", yearBeforeStart, realData.getAllYears());
            }
        }
        //data
        rowIndex++;
        for(int y: years) {
            columnIndex = 0;
            data.setInt(rowIndex, columnIndex++, y);
            for(String zip: validZips) {
                double simu = simuData.getCount(y, product, zip);
                double real = realData.hasZip(zip) ? realData.get(cumulated, y, zip) : 0;
                data.setDouble(rowIndex, columnIndex++, simu);
                data.setDouble(rowIndex, columnIndex++, real);
            }
            rowIndex++;
        }

        zipCounter.set(validZips.size());
        return data;
    }
}
