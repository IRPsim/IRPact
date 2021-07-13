package de.unileipzig.irpact.core.postprocessing.image;

/**
 * @author Daniel Abitz
 */
public class BasicRealAdoptionData implements RealAdoptionData {

    protected int value;

    public BasicRealAdoptionData() {
        this(0);
    }

    public BasicRealAdoptionData(int value) {
        setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int get(int year, String zip) {
        return value;
    }
}
