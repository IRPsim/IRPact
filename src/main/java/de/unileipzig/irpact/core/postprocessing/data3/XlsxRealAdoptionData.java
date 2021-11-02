package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

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

        BasicScaledRealAdoptionData scaledRealAdoptionData = new BasicScaledRealAdoptionData();
        scaledRealAdoptionData.initEmpty();
        scaledRealAdoptionData.setScale(scale);
        Rnd rnd = fixError ?
                new Rnd("_FIX_ERRORS_")
                : null;
        for(int year: years) {
            updateScaledAdoptionData(scaledRealAdoptionData, year, zips, scale, rnd);
        }
        scaledRealAdoptionData.resetZips();
        scaledRealAdoptionData.resetYears();
        scaledAdoptionData.put(scale, scaledRealAdoptionData);
    }

    protected void updateScaledAdoptionData(
            BasicScaledRealAdoptionData scaledRealAdoptionData,
            int year,
            Set<String> zips,
            double scale,
            Rnd rnd) {
        LOGGER.trace("build scaled initial adoption data for year '{}', scale '{}', zips '{}'", year, scale, zips);

        int totalUnscaled = 0;
        int totalScaled = 0;

        Map<String, Integer> unscaledZips = new HashMap<>();
        Map<String, Integer> scaledZips = new HashMap<>();
        Map<String, Integer> remainingZips = new HashMap<>();
        LOGGER.trace("scaled initial adoption data:");
        for(String zip: zips) {
            int unscaled = Math.max(0, getUncumulated(year, zip));
            if(unscaled > 0) {
                totalUnscaled += unscaled;
            }

            int scaled = Math.max(0, (int) (scale * unscaled));
            if(scaled > 0) {
                totalScaled += scaled;
            }

            int remaining = Math.max(0, unscaled - scaled);
            LOGGER.trace("zip '{}': unscaled={}, scaled={}, remaining={}", zip, unscaled, scaled, remaining);

            unscaledZips.put(zip, unscaled);
            scaledZips.put(zip, scaled);
            remainingZips.put(zip, remaining);
        }


        int realTotalScaled = (int) (scale * totalUnscaled);
        int error = realTotalScaled - totalScaled;
        LOGGER.trace("error={} (totalScaled={}, realTotalScaled={}), fix error: {}", error, totalScaled, realTotalScaled, rnd != null);

        if(error > 0 && rnd != null) {
            for(int i = 1; i <= error; i++) {
                LOGGER.trace("fix error ({}/{})", i, error);
                WeightedMapping<String> zipMapping = buildWeightedMapping(unscaledZips, remainingZips);
                if(zipMapping.isEmpty()) {
                    LOGGER.warn("not enough data");
                    break;
                }

                String zip = zipMapping.getWeightedRandom(rnd);
                remainingZips.put(zip, remainingZips.get(zip) - 1);
                scaledZips.put(zip, scaledZips.get(zip) + 1);
                LOGGER.trace("update zip '{}': remaining={}, scaled={}", zip, remainingZips.get(zip), scaledZips.get(zip));
            }
        }

        LOGGER.trace("final scaled initial adoption data:");
        int firstYear = getFirstYear();
        for(String zip: unscaledZips.keySet()) {
            int unscaled = unscaledZips.get(zip);
            int scaled = scaledZips.getOrDefault(zip, 0);
            scaledRealAdoptionData.setUncumulated(zip, year, scaled);
            for(int y = firstYear; y <= year; y++) {
                scaledRealAdoptionData.updateCumulated(zip, y, scaled);
            }
            LOGGER.trace("zip '{}': unscaled={}, scaled={}", zip, unscaled, scaled);

            LOGGER.trace(
                    "[VALIDATION] zip={}, year={}: scaledUncumulated={}, scaledCumulated={}, realUncumulated={}, realCumulated={}",
                    zip,
                    year,
                    scaledRealAdoptionData.getUncumulated(year, zip),
                    scaledRealAdoptionData.getCumulated(year, zip),
                    getUncumulated(year, zip),
                    getUncumulated(year, zip));
        }
    }

    protected WeightedMapping<String> buildWeightedMapping(
            Map<String, Integer> unscaledZip,
            Map<String, Integer> remainingZip) {
        NavigableMapWeightedMapping<String> mapping = new NavigableMapWeightedMapping<>();
        for(String zip: unscaledZip.keySet()) {
            int rem = remainingZip.getOrDefault(zip, 0);
            LOGGER.trace("zip '{}' remaining: {}", zip, rem);
            if(rem > 0) {
                mapping.set(zip, unscaledZip.get(zip));
            }
        }
        return mapping;
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
            years = new LinkedHashSet<>();
            cumulated.getN().stream()
                    .map(Integer::parseInt)
                    .forEach(y -> years.add(y));
            uncumulated.getN().stream()
                    .map(Integer::parseInt)
                    .forEach(y -> years.add(y));
        }
        return years;
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
}
