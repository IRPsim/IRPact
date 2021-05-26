package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class CumulativeAdoptionsYearX extends AbstractCumulativeAdoptionAnalyser {

    public static final int INDEX_KEY = 1;
    public static final int INDEX_ADOPTIONS = 2;

    protected String key;

    public CumulativeAdoptionsYearX() {
        super(new VarCollection(Integer.class, String.class, Integer.class));
    }

    public CumulativeAdoptionsYearX(Collection<? extends Integer> years, Collection<? extends String> zips, String key) {
        this();
        setYears(years);
        init(zips);
        setKey(key);
    }

    public void init(Collection<? extends String> keys) {
        for(Integer year: years) {
            for(String key: keys) {
                data.varSet(year, key, ZERO.get());
            }
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getTotal(AdoptionInfo info) {
        String value = findString(info, getKey());
        return data.filteredStream(INDEX_KEY, _value -> Objects.equals(value, _value))
                .mapToInt(entry -> (Integer) entry[INDEX_ADOPTIONS])
                .sum();
    }

    @Override
    protected void add(int year, AdoptionInfo info) {
        String value = findString(info, getKey());
        data.varUpdate(ZERO, ADD, year, value, ONE.get());
    }
}
