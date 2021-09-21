package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class XlsxRealAdoptionData implements RealAdoptionData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxRealAdoptionData.class);

    protected Set<String> zips;
    protected Set<Integer> years;
    protected TypedMatrix<String, String, Integer> cumulated;
    protected TypedMatrix<String, String, Integer> uncumulated;

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
    public Set<String> getAllZips() {
        if(zips == null) {
            zips = new LinkedHashSet<>(cumulated.getM());
            zips.addAll(uncumulated.getM());
        }
        return zips;
    }

    @Override
    public boolean hasZip(String zip) {
        return getAllZips().contains(zip);
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
}
