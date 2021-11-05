package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.data.MapBasedTypedMatrix;

/**
 * @author Daniel Abitz
 */
public class BasicScaledRealAdoptionData extends XlsxRealAdoptionData implements ScaledRealAdoptionData {

    protected double scale;

    protected BasicScaledRealAdoptionData() {
    }

    protected void initEmpty() {
        cumulated = new MapBasedTypedMatrix<>();
        uncumulated = new MapBasedTypedMatrix<>();
    }

    protected void updateCumulated(String zip, String year, int delta) {
        int current = cumulated.getOrDefault(zip, year, 0);
        cumulated.set(zip, year, current + delta);
    }

    protected void updateCumulated(String zip, int year, int delta) {
        updateCumulated(zip, Integer.toString(year), delta);
    }

    protected void updateUncumulated(String zip, String year, int delta) {
        int current = uncumulated.getOrDefault(zip, year, 0);
        uncumulated.set(zip, year, current + delta);
    }

    protected void updateUncumulated(String zip, int year, int delta) {
        updateUncumulated(zip, Integer.toString(year), delta);
    }

    protected void setUncumulated(String zip, String year, int count) {
        uncumulated.set(zip, year, count);
    }

    protected void setUncumulated(String zip, int year, int count) {
        setUncumulated(zip, Integer.toString(year), count);
    }

    protected void setCumulated(String zip, String year, int count) {
        cumulated.set(zip, year, count);
    }

    protected void setCumulated(String zip, int year, int count) {
        setCumulated(zip, Integer.toString(year), count);
    }

    protected void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public double getScale() {
        return scale;
    }
}
