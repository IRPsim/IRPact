package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class CumulativeAdoptionsYearXY extends AbstractCumulativeAdoptionAnalyser {

    public static final int INDEX_XKEY = 1;
    public static final int INDEX_YKEY = 2;
    public static final int INDEX_ADOPTIONS = 3;

    protected String xKey;
    protected String yKey;

    public CumulativeAdoptionsYearXY() {
        super(new VarCollection(Integer.class, String.class, Integer.class));
    }

    public CumulativeAdoptionsYearXY(
            Collection<? extends Integer> years,
            Collection<? extends String> xKeys,
            Collection<? extends String> yKeys,
            String xKey,
            String yKey) {
        this();
        setYears(years);
        init(xKeys, yKeys);
        setXKey(xKey);
        setYKey(yKey);
    }

    public void init(
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
    public int getTotal(AdoptionInfo info) {
        String xData = findString(info, getXKey());
        String yData = findString(info, getYKey());
        return data.filteredStream(
                null,
                _xValue -> Objects.equals(_xValue, xData),
                _yValue -> Objects.equals(_yValue, yData),
                null)
                .mapToInt(entry -> (Integer) entry[INDEX_ADOPTIONS])
                .sum();
    }

    @Override
    protected void add(int year, AdoptionInfo info) {
        String xData = findString(info, getXKey());
        String yData = findString(info, getYKey());
        data.varUpdate(ZERO, ADD, year, xData, yData, 1);
    }
}
