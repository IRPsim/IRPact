package de.unileipzig.irpact.core.postprocessing.image;

import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.AdoptionResultInfo;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.AnnualCumulativeAdoptionsPhase;
import de.unileipzig.irpact.core.postprocessing.data.adoptions.AnnualCumulativeAdoptionsZip;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public final class DataMapper {

    public static final Function<? super String, ? extends String> R_ESCAPE = str ->
            str;
    public static final Function<? super String, ? extends String> GNUPLOT_ESCAPE = str ->
            str.replace("_", "\\\\\\_");

    private DataMapper() {
    }

    //=========================
    //R
    //=========================

    public static List<List<String>> toRData(
            AnnualCumulativeAdoptionsZip analyser,
            Function<? super String, ? extends String> strMapper) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                strMapper.apply("year"),
                strMapper.apply("zip"),
                strMapper.apply("adoptions")
        ));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    strMapper.apply(Integer.toString(year)),
                    strMapper.apply(zip),
                    strMapper.apply(result.printValue())
            ));
        }
        return csvData;
    }

    public static List<List<String>> toRDataWithRealData(
            AnnualCumulativeAdoptionsZip analyser,
            RealAdoptionData realData,
            String noStr, String yesStr,
            Function<? super String, ? extends String> strMapper) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                strMapper.apply("year"),
                strMapper.apply("zip"),
                strMapper.apply("adoptions"),
                strMapper.apply("real")
        ));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    strMapper.apply(Integer.toString(year)),
                    strMapper.apply(zip),
                    strMapper.apply(result.printValue()),
                    strMapper.apply(noStr)
            ));

            csvData.add(Arrays.asList(
                    strMapper.apply(Integer.toString(year)),
                    strMapper.apply(zip),
                    strMapper.apply(Integer.toString(realData.getUncumulated(year, zip))),
                    strMapper.apply(yesStr)
            ));
        }
        return csvData;
    }

    public static List<List<String>> toRDataWithCumulativeValue(
            AnnualCumulativeAdoptionsPhase analyser,
            Function<? super AdoptionPhase, ? extends String> printer,
            Function<? super String, ? extends String> strMapper) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                strMapper.apply("year"),
                strMapper.apply("phase"),
                strMapper.apply("adoptionsCumulative")
        ));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsPhase.INDEX_YEAR];
            AdoptionPhase phase = (AdoptionPhase) entry[AnnualCumulativeAdoptionsPhase.INDEX_PHASE];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    strMapper.apply(Integer.toString(year)),
                    strMapper.apply(printer.apply(phase)),
                    strMapper.apply(result.printCumulativeValue())
            ));
        }
        return csvData;
    }

    //=========================
    //gnuplot
    //=========================

    public static List<List<String>> toGnuPlotData(
            AnnualCumulativeAdoptionsZip analyser,
            Function<? super String, ? extends String> strMapper) {
        Set<Integer> years = new TreeSet<>();
        Map<String, Map<Integer, Integer>> zipData = new LinkedHashMap<>();
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            years.add(year);
            zipData.computeIfAbsent(zip, _zip -> new TreeMap<>()).put(year, result.getValue());
        }

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(strMapper.apply("years"));
        for(String zip: zipData.keySet()) {
            header.add(strMapper.apply(zip));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(strMapper.apply(Integer.toString(year)));
            for(Map<Integer, Integer> zipEntry: zipData.values()) {
                row.add(strMapper.apply(Integer.toString(zipEntry.get(year))));
            }
            csvData.add(row);
        }
        return csvData;
    }

    public static List<List<String>> toGnuPlotDataWithRealData(
            AnnualCumulativeAdoptionsZip analyser,
            RealAdoptionData realData,
            Function<? super String, ? extends String> strMapper) {
        Set<Integer> years = new TreeSet<>();
        Map<String, Map<Integer, Integer>> zipData = new LinkedHashMap<>();
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            years.add(year);
            zipData.computeIfAbsent(zip, _zip -> new TreeMap<>()).put(year, result.getValue());
        }

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(strMapper.apply("years"));
        for(String zip: zipData.keySet()) {
            header.add(strMapper.apply(zip));
            header.add(strMapper.apply(zip + "-R"));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(strMapper.apply(Integer.toString(year)));
            for(Map.Entry<String, Map<Integer, Integer>> zipEntry: zipData.entrySet()) {
                row.add(strMapper.apply(Integer.toString(zipEntry.getValue().get(year))));
                row.add(strMapper.apply(Integer.toString(realData.getUncumulated(year, zipEntry.getKey()))));
            }
            csvData.add(row);
        }
        return csvData;
    }

    public static List<List<String>> toGnuPlotDataWithCumulativeValue(
            AnnualCumulativeAdoptionsPhase analyser,
            Function<? super AdoptionPhase, ? extends String> printer,
            Function<? super String, ? extends String> strMapper) {
        Set<Integer> years = new TreeSet<>();
        Map<AdoptionPhase, Map<Integer, Integer>> phaseData = new LinkedHashMap<>();
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsPhase.INDEX_YEAR];
            AdoptionPhase phase = (AdoptionPhase) entry[AnnualCumulativeAdoptionsPhase.INDEX_PHASE];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            years.add(year);
            phaseData.computeIfAbsent(phase, _phase -> new TreeMap<>()).put(year, result.getCumulativeValue());
        }

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(strMapper.apply("years"));
        for(AdoptionPhase phase: phaseData.keySet()) {
            header.add(strMapper.apply(printer.apply(phase)));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(strMapper.apply(Integer.toString(year)));
            for(Map<Integer, Integer> zipEntry: phaseData.values()) {
                row.add(strMapper.apply(Integer.toString(zipEntry.get(year))));
            }
            csvData.add(row);
        }
        return csvData;
    }
}
