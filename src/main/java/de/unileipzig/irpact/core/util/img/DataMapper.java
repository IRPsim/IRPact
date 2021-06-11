package de.unileipzig.irpact.core.util.img;

import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.result.adoptions.AdoptionResultInfo;
import de.unileipzig.irpact.core.util.result.adoptions.AnnualCumulativeAdoptionsPhase;
import de.unileipzig.irpact.core.util.result.adoptions.AnnualCumulativeAdoptionsZip;

import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public final class DataMapper {

    private DataMapper() {
    }

    //=========================
    //R
    //=========================

    public static List<List<String>> toRData(AnnualCumulativeAdoptionsZip analyser) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList("year", "zip", "adoptions"));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    Integer.toString(year),
                    zip,
                    result.printValue()
            ));
        }
        return csvData;
    }

    public static List<List<String>> toRDataWithRealData(AnnualCumulativeAdoptionsZip analyser, RealAdoptionData realData) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList("year", "zip", "adoptions", "real"));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsZip.INDEX_YEAR];
            String zip = (String) entry[AnnualCumulativeAdoptionsZip.INDEX_ZIP];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    Integer.toString(year),
                    zip,
                    result.printValue(),
                    "no"
            ));

            csvData.add(Arrays.asList(
                    Integer.toString(year),
                    zip,
                    Integer.toString(realData.get(year, zip)),
                    "yes"
            ));
        }
        return csvData;
    }

    public static List<List<String>> toRDataWithCumulativeValue(
            AnnualCumulativeAdoptionsPhase analyser,
            Function<? super AdoptionPhase, ? extends String> printer) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList("year", "phase", "adoptionsCumulative"));
        for(Object[] entry: analyser.getData().iterable()) {
            int year = (Integer) entry[AnnualCumulativeAdoptionsPhase.INDEX_YEAR];
            AdoptionPhase phase = (AdoptionPhase) entry[AnnualCumulativeAdoptionsPhase.INDEX_PHASE];
            AdoptionResultInfo result = (AdoptionResultInfo) entry[AnnualCumulativeAdoptionsZip.INDEX_ADOPTIONS];

            csvData.add(Arrays.asList(
                    Integer.toString(year),
                    printer.apply(phase),
                    result.printCumulativeValue()
            ));
        }
        return csvData;
    }

    //=========================
    //gnuplot
    //=========================

    public static List<List<String>> toGnuPlotData(AnnualCumulativeAdoptionsZip analyser) {
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
        header.add("years");
        header.addAll(zipData.keySet());
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(Integer.toString(year));
            for(Map<Integer, Integer> zipEntry: zipData.values()) {
                row.add(Integer.toString(zipEntry.get(year)));
            }
            csvData.add(row);
        }
        return csvData;
    }

    public static List<List<String>> toGnuPlotDataWithRealData(AnnualCumulativeAdoptionsZip analyser, RealAdoptionData realData) {
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
        header.add("years");
        for(String zip: zipData.keySet()) {
            header.add(zip + "-S");
            header.add(zip + "-R");
        }
        header.addAll(zipData.keySet());
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(Integer.toString(year));
            for(Map.Entry<String, Map<Integer, Integer>> zipEntry: zipData.entrySet()) {
                row.add(Integer.toString(zipEntry.getValue().get(year)));
                row.add(Integer.toString(realData.get(year, zipEntry.getKey())));
            }
            csvData.add(row);
        }
        return csvData;
    }

    public static List<List<String>> toGnuPlotDataWithCumulativeValue(
            AnnualCumulativeAdoptionsPhase analyser,
            Function<? super AdoptionPhase, ? extends String> printer) {
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
        header.add("years");
        for(AdoptionPhase phase: phaseData.keySet()) {
            header.add(printer.apply(phase));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(Integer.toString(year));
            for(Map<Integer, Integer> zipEntry: phaseData.values()) {
                row.add(Integer.toString(zipEntry.get(year)));
            }
            csvData.add(row);
        }
        return csvData;
    }
}
