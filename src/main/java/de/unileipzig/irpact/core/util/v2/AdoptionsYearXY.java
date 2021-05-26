package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class AdoptionsYearXY extends AbstractAdoptionAnalyser {

    protected String xKey;
    protected String yKey;

    public AdoptionsYearXY() {
        super(new VarCollection(Integer.class, String.class, String.class, Integer.class));
    }

    public AdoptionsYearXY(
            Collection<? extends Integer> years,
            Collection<? extends String> xKeys,
            Collection<? extends String> yKeys,
            String xKey,
            String yKey) {
        this();
        init(years, xKeys, yKeys);
        setXKey(xKey);
        setYKey(yKey);
    }

    public void init(
            Collection<? extends Integer> years,
            Collection<? extends String> xKeys,
            Collection<? extends String> yKeys) {
        for(Integer year: years) {
            for(String xKey: xKeys) {
                for(String yKey: yKeys) {
                    data.varSet(year, xKey, yKey, ZERO.get());
                }
            }
        }
    }

    public void setXKey(String xKey) {
        this.xKey = xKey;
    }

    public String getXKey() {
        return xKey;
    }

    public void setYKey(String yKey) {
        this.yKey = yKey;
    }

    public String getYKey() {
        return yKey;
    }

    @Override
    public void add(AdoptionInfo info) {
        String xData = findString(info, getXKey());
        String yData = findString(info, getYKey());
        data.varUpdate(ZERO, ADD, info.getYear(), xData, yData, ONE.get());
    }
}
