package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AdoptionsYearX extends AbstractAdoptionAnalyser {

    protected String key;

    public AdoptionsYearX() {
        super(new VarCollection(Integer.class, String.class, Integer.class));
    }

    public AdoptionsYearX(Collection<? extends Integer> years, Collection<? extends String> zips, String key) {
        this();
        init(years, zips);
        setKey(key);
    }

    public void init(
            Collection<? extends Integer> years,
            Collection<? extends String> keys) {
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
    public void add(AdoptionInfo info) {
        String value = findString(info, getKey());
        data.varUpdate(ZERO, ADD, info.getYear(), value, ONE.get());
    }
}
