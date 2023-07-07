package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class XlsxRealAdoptionData implements RealAdoptionData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxRealAdoptionData.class);

    protected Set<String> zips;
    protected Set<Integer> years;
    protected TypedMatrix<String, String, Integer> cumulated;
    protected TypedMatrix<String, String, Integer> uncumulated;
    protected Map<Double, ScaledRealAdoptionData> scaledAdoptionData = new HashMap<>();

    public XlsxRealAdoptionData() {
    }

    @Override
    public String printCumulatedData() {
        return printData(cumulated);
    }

    @Override
    public String printUncumulatedData() {
        return printData(uncumulated);
    }

    protected String printData(TypedMatrix<String, String, Integer> matrix) {
        StringBuilder sb = new StringBuilder();
        for(int year: getAllYears()) {
            for(String zip: getAllZips()) {
                int count = matrix.getOrDefault(zip, Integer.toString(year), -1);
                sb.append(year)
                        .append(";").append(zip)
                        .append(";").append(count);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void setCumulated(TypedMatrix<String, String, Integer> data) {
        this.cumulated = data;
    }

    public TypedMatrix<String, String, Integer> getCumulated() {
        return cumulated;
    }

    public void setUncumulated(TypedMatrix<String, String, Integer> uncumulated) {
        this.uncumulated = uncumulated;
    }

    public TypedMatrix<String, String, Integer> getUncumulated() {
        return uncumulated;
    }

    protected void initialize(Collection<Integer> years, Collection<String> zips) {
        for(String zip: zips) {
            for(int year: years) {
                cumulated.set(zip, Integer.toString(year), 0);
                uncumulated.set(zip, Integer.toString(year), 0);
            }
        }
    }

    @Override
    public void createScaledAdoptionData(double scale, boolean fixError) {
        LOGGER.trace("build scaled adoption data (scale={}, fixError={})", scale, fixError);
        if(scale < 0) {
            throw new IllegalArgumentException("negative scale: " + scale);
        }
        if(scale > 1) {
            throw new IllegalArgumentException("scale too large: " + scale);
        }
        if(scaledAdoptionData.containsKey(scale)) {
            throw new IllegalStateException("scale " + scale + " already exists");
        }

        Set<String> zips = getAllZips();
        Set<Integer> years = getAllYears();
        List<Integer> sortedYears = years.stream()
                .sorted()
                .collect(Collectors.toList());

        BasicScaledRealAdoptionData scaledRealAdoptionData = new BasicScaledRealAdoptionData();
        scaledRealAdoptionData.initEmpty();
        scaledRealAdoptionData.setScale(scale);
        Rnd rnd = fixError ?
                new Rnd("_FIX_ERRORS_")
                : null;
        updateScaledAdoptionData(this, scaledRealAdoptionData, sortedYears, zips, scale, rnd);
        scaledRealAdoptionData.resetZips();
        scaledRealAdoptionData.resetYears();
        scaledAdoptionData.put(scale, scaledRealAdoptionData);
    }

    @Override
    public boolean hasScaledAdoptionData(double scale) {
        return scaledAdoptionData.containsKey(scale);
    }

    @Override
    public ScaledRealAdoptionData getScaledAdoptionData(double scale) {
        if(scaledAdoptionData.containsKey(scale)) {
            return scaledAdoptionData.get(scale);
        } else {
            throw new NoSuchElementException("no data for scale " + scale);
        }
    }

    @Override
    public Set<String> getAllZips() {
        if(zips == null) {
            zips = new LinkedHashSet<>(cumulated.getM());
            zips.addAll(uncumulated.getM());
        }
        return zips;
    }

    protected void resetZips() {
        zips = null;
        getAllZips();
    }

    @Override
    public boolean hasZip(String zip) {
        return getAllZips().contains(zip);
    }

    @Override
    public boolean hasYear(int year) {
        return getAllYears().contains(year);
    }

    @Override
    public void getValidZips(Collection<? extends String> input, Collection<? super String> output) {
        for(String zip: input) {
            if(hasZip(zip)) {
                output.add(zip);
            }
        }
    }

    @Override
    public void getInvalidZips(Collection<? extends String> input, Collection<? super String> output) {
        for(String zip: input) {
            if(!hasZip(zip)) {
                output.add(zip);
            }
        }
    }

    @Override
    public void getUnusedZips(Collection<? extends String> input, Collection<? super String> output) {
        for(String zip: getAllZips()) {
            if(!input.contains(zip)) {
                output.add(zip);
            }
        }
    }

    @Override
    public Set<Integer> getAllYears() {
        if(years == null) {
            Set<Integer> cumulatedYears = cumulated.getN().stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            Set<Integer> uncumulatedYears = uncumulated.getN().stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            years = new TreeSet<>(cumulatedYears);
            years.retainAll(uncumulatedYears);
        }
        return years;
    }

    public List<Integer> getRemainingYears(int from, boolean inclusive) {
        return getAllYears().stream()
                .filter(y -> inclusive ? y >= from : y > from)
                .sorted()
                .collect(Collectors.toList());
    }

    protected void resetYears() {
        years = null;
        getAllYears();
    }

    public int getFirstYear() {
        OptionalInt firstYear = getAllYears().stream()
                .mapToInt(i -> i)
                .min();
        if(firstYear.isPresent()) {
            return firstYear.getAsInt();
        } else {
            throw new IllegalStateException("no data");
        }
    }

    public int getLastYear() {
        OptionalInt lastYear = getAllYears().stream()
                .mapToInt(i -> i)
                .max();
        if(lastYear.isPresent()) {
            return lastYear.getAsInt();
        } else {
            throw new IllegalStateException("no data");
        }
    }

    @Override
    public int getCumulated(int year, String zip) {
        if(cumulated == null) {
            throw new NullPointerException("cumulated");
        }
        int adoptions = cumulated.getOrDefault(zip, Integer.toString(year), -1);
        if(adoptions == -1) {
            LOGGER.warn("missing adoption data for zip '{}' and year '{}'", zip, year);
            return 0;
        } else {
            return adoptions;
        }
    }

    @Override
    public int getUncumulated(int year, String zip) {
        if(uncumulated == null) {
            throw new NullPointerException("uncumulated");
        }
        int adoptions = uncumulated.getOrDefault(zip, Integer.toString(year), -1);
        if(adoptions == -1) {
            LOGGER.warn("missing adoption data for zip '{}' and year '{}'", zip, year);
            return 0;
        } else {
            return adoptions;
        }
    }

    @Override
    public int getCumulated(int year, Collection<? extends String> zips) {
        int total = 0;
        for(String zip: zips) {
            total += getCumulated(year, zip);
        }
        return total;
    }

    @Override
    public int getUncumulated(int year, Collection<? extends String> zips) {
        int total = 0;
        for(String zip: zips) {
            total += getUncumulated(year, zip);
        }
        return total;
    }

    @Override
    public int getCumulated(int year) {
        return getCumulated(year, getAllZips());
    }

    @Override
    public int getUncumulated(int year) {
        return getUncumulated(year, getAllZips());
    }

    //==================================================
    //TEST2
    //==================================================

    private static void printYearZips(XlsxRealAdoptionData data0, XlsxRealAdoptionData data1) {
        for(int y: data0.getAllYears()) {
            System.out.println(y + ": " + data0.getCumulated(y) + " " + data1.getCumulated(y));
            for(String zip: data0.getAllZips()) {
                System.out.println("  " + zip + ": " + data0.getCumulated(y, zip) + " " + data1.getCumulated(y, zip));
            }
        }
    }

    private static WeightedMapping<String> buildWeightedMapping(Map<String, Integer> weightMap) {
        NavigableMapWeightedMapping<String> mapping = new NavigableMapWeightedMapping<>();
        for(String zip: weightMap.keySet()) {
            mapping.set(zip, weightMap.get(zip));
        }
        return mapping;
    }

    private static void updateScaledAdoptionData(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            List<Integer> sortedYears,
            Set<String> zips,
            double scale,
            Rnd rnd) {
        //phase 1
        scaleDirect(originalData, scaledData, sortedYears, zips, scale);

        //phase 2
        repair(scaledData, sortedYears, zips);
        validate(scaledData, sortedYears, zips);

        //phase 3
        repairProbabilistic(originalData, scaledData, sortedYears, zips, scale, rnd);
        validate(scaledData, sortedYears, zips);
    }

    private static void scaleDirect(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            List<Integer> sortedYears,
            Set<String> zips,
            double scale) {
        for(Integer currentYear: sortedYears) {
            for(String zip: zips) {
                scaleDirect(originalData, scaledData, currentYear, zip, scale);
            }
        }
    }

    private static void scaleDirect(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            int currentYear,
            String zip,
            double scale) {

        int uncumulatedUnscaled = originalData.getUncumulated(currentYear, zip);
        int cumulatedUnscaled = originalData.getCumulated(currentYear, zip);

        int uncumulatedScaled = (int) (scale * uncumulatedUnscaled);
        int cumulatedScaled = (int) (scale * cumulatedUnscaled);

        scaledData.setUncumulated(zip, currentYear, uncumulatedScaled);
        scaledData.setCumulated(zip, currentYear, cumulatedScaled);

        LOGGER.trace("[scaleDirect, year={}] scale={}, uncumulated: {} -> {}, cumulated: {} -> {}",
                currentYear,
                scale,
                uncumulatedUnscaled, uncumulatedScaled,
                cumulatedUnscaled, cumulatedScaled
        );
    }

    private static void validate(BasicScaledRealAdoptionData scaledData, List<Integer> years, Set<String> zips) {
        for(String zip: zips) {
            for(int i = 0; i < years.size(); i++) {
                int currentYear = years.get(i);
                if(i > 0) {
                    int lastYear = years.get(i - 1);
                    int cumulated = scaledData.getCumulated(currentYear, zip);
                    int calcCumulated = scaledData.getUncumulated(currentYear, zip) + scaledData.getCumulated(lastYear, zip);
                    if(cumulated != calcCumulated) {
                        throw new IllegalArgumentException(StringUtil.format("mismatch year={}, zip={}", currentYear, zip));
                    }
                } else {
                    int cumulated = scaledData.getCumulated(currentYear, zip);
                    int uncumulated = scaledData.getUncumulated(currentYear, zip);
                    if(cumulated != uncumulated) {
                        throw new IllegalArgumentException(StringUtil.format("mismatch year={}, zip={}", currentYear, zip));
                    }
                }
            }
        }
    }

    private static void repair(
            BasicScaledRealAdoptionData scaledData,
            List<Integer> sortedYears,
            Set<String> zips) {
        Integer lastYear = null;
        for(Integer currentYear: sortedYears) {
            if(lastYear != null) {
                for(String zip : zips) {
                    repair(scaledData, lastYear, currentYear, zip);
                }
            }
            lastYear = currentYear;
        }
    }

    private static void repair(
            BasicScaledRealAdoptionData scaledData,
            int lastYear,
            int currentYear,
            String zip) {

        int currentUncumulated = scaledData.getUncumulated(currentYear, zip);
        int lastCumulated = scaledData.getCumulated(lastYear, zip);
        int tempCumulated = lastCumulated + currentUncumulated;

        int currentCumulated = scaledData.getCumulated(currentYear, zip);

        int diff = currentCumulated - tempCumulated;

        LOGGER.trace("[repair, year={}, zip={}] cumulatedScaled={}, tempCumulated={} ({} + {}), diff={}",
                currentYear,
                zip,
                currentCumulated,
                tempCumulated,
                lastCumulated,
                currentUncumulated,
                diff
        );

        if(diff < 0) {
            throw new IllegalStateException(StringUtil.format("negative diff for year={}, zip={}", currentYear, zip));
        }

        if(diff > 0) {
            scaledData.updateUncumulated(zip, currentYear, diff);

            int newUncumulatedScaled = scaledData.getUncumulated(currentYear, zip);
            int newTempCumulated = lastCumulated + newUncumulatedScaled;

            LOGGER.trace("[repaired, year={}, zip={}] cumulatedScaled={}, newTempCumulated={} ({} + {})",
                    currentYear,
                    zip,
                    scaledData.getCumulated(currentYear, zip),
                    newTempCumulated,
                    lastCumulated,
                    newUncumulatedScaled
            );
        }
    }

    private static void repairProbabilistic(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            List<Integer> sortedYears,
            Set<String> zips,
            double scale,
            Rnd rnd) {
        if(rnd != null) {
            for(Integer currentYear: sortedYears) {
                repairProbabilistic(originalData, scaledData, currentYear, zips, scale, rnd);
            }
        }
    }

    private static void repairProbabilistic(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            int year,
            Set<String> zips,
            double scale,
            Rnd rnd) {

        int totalUnscaledCumulative = originalData.getCumulated(year, zips);
        int totalCustomScaledCumulative = (int) (scale * totalUnscaledCumulative);
        int totalScaledCumulative = scaledData.getCumulated(year, zips);
        int totalDiff = totalCustomScaledCumulative - totalScaledCumulative;

        LOGGER.trace(
                "[repairProbabilistic, year={}] totalCustomScaledCumulative={} ({} * {}), totalScaledCumulative={}, totalDiff={}",
                year,
                totalCustomScaledCumulative,
                scale,
                totalUnscaledCumulative,
                totalScaledCumulative,
                totalDiff
        );

        if(totalDiff < 0) {
            LOGGER.warn("negative diff in year={}", year);
            return;
        }

        if(totalDiff > 0) {
            LOGGER.trace("[repairProbabilistic] collect zip weights");
            Map<String, Integer> weights = new HashMap<>();
            for(String zip: zips) {
                int unscaled = originalData.getCumulated(year, zip);
                int scaled = scaledData.getCumulated(year, zip);
                int rem = unscaled - scaled;
                LOGGER.trace("[year={}, zip={}] unscaled={}, scaled={}, rem={}", year, zip, unscaled, scaled, rem);

                if(rem > 0) {
                    weights.put(zip, rem);
                }
            }

            LOGGER.trace("[repairProbabilistic] fix {} errors", totalDiff);
            for(int i = 0; i < totalDiff; i++) {
                WeightedMapping<String> weightMap = buildWeightedMapping(weights);

                String zipDraw = weightMap.getWeightedRandom(rnd);

                int currentWeight = weights.get(zipDraw);
                int newWeight = currentWeight - 1;

                if(newWeight == 0) {
                    weights.remove(zipDraw);
                } else {
                    weights.put(zipDraw, newWeight);
                }

                scaledData.updateUncumulated(zipDraw, year, 1);

                List<Integer> remYears = scaledData.getRemainingYears(year, true);
                for(int y: remYears) {
                    int currentValue = scaledData.getCumulated(y, zipDraw);
                    scaledData.updateCumulated(zipDraw, y, 1);
                    LOGGER.trace("[repairing, year={}, zip={}] {} -> {}", y, zipDraw, currentValue, scaledData.getCumulated(y, zipDraw));
                }

                LOGGER.trace("[repaired, year={}] error={}/{} zipDraw={} (newWeight={}) (years={})", year, (i+1), totalDiff, zipDraw, newWeight, remYears);
            }
        }
    }

    //==================================================
    //Variante 3
    //==================================================

    private static void updateScaledAdoptionData2(
            XlsxRealAdoptionData originalData,
            BasicScaledRealAdoptionData scaledData,
            List<Integer> sortedYears,
            Set<String> zips,
            double scale,
            Rnd rnd) {

        scaledData.initialize(sortedYears, zips);

        for(int currentYear: sortedYears) {
            int unscaled = originalData.getCumulated(currentYear, zips);
            int calcScaled = (int) (scale * unscaled);
            int scaled = scaledData.getCumulated(currentYear, zips);
            int diff = calcScaled - scaled;

            if(diff == 0) {
                continue;
            }

            if(diff < 0) {
                LOGGER.warn("negative diff for year={}", currentYear);
                continue;
            }

            Map<String, Integer> weights = new LinkedHashMap<>();
            for(String zip : zips) {
                int w = originalData.getUncumulated(currentYear, zip);
                if(w > 0) {
                    weights.put(zip, w);
                }
            }

            for(int j = 0; j < diff; j++) {
                LOGGER.trace("year={}, fix error {}/{}", currentYear, (j + 1), diff);
                WeightedMapping<String> weightedMapping = buildWeightedMapping(weights);
                String zipDraw = weightedMapping.getWeightedRandom(rnd);

                int currentWeight = weights.get(zipDraw);
                int newWeight = currentWeight - 1;

                if (newWeight == 0) {
                    weights.remove(zipDraw);
                } else {
                    weights.put(zipDraw, newWeight);
                }

                scaledData.updateUncumulated(zipDraw, currentYear, 1);
                LOGGER.trace(
                        "[update uncumulative] year={}, zip={}, uncumulated={}",
                        currentYear,
                        zipDraw,
                        scaledData.getUncumulated(currentYear, zipDraw)
                );

                List<Integer> remYears = scaledData.getRemainingYears(currentYear, true);
                for(int y : remYears) {
                    scaledData.updateCumulated(zipDraw, y, 1);
                    LOGGER.trace(
                            "[update cumulative] year={}, zip={}, cumulated={}",
                            y,
                            zipDraw,
                            scaledData.getCumulated(y, zipDraw)
                    );
                }
            }
        }
    }
}
