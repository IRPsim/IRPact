package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class RAModelData {

    public static final int DEFAULT_ADOPTER_POINTS = 3;
    public static final int DEFAULT_INTERESTED_POINTS = 2;
    public static final int DEFAULT_AWARE_POINTS = 1;
    public static final int DEFAULT_UNKNOWN_POINTS = 3;

    protected Map<Integer, NPVMatrix> npData;

    protected double a;
    protected double b;
    protected double c;
    protected double d;

    protected int adopterPoints = DEFAULT_ADOPTER_POINTS;
    protected int interestedPoints = DEFAULT_INTERESTED_POINTS;
    protected int awarePoints = DEFAULT_AWARE_POINTS;
    protected int unknownPoints = DEFAULT_UNKNOWN_POINTS;

    public RAModelData() {
        this(new HashMap<>());
    }

    public RAModelData(Map<Integer, NPVMatrix> npData) {
        this.npData = npData;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    public void put(int year, NPVMatrix matrix) {
        npData.put(year, matrix);
    }

    public double NPV(ConsumerAgent agent, int year) {
        NPVMatrix matrix = npData.get(year);
        int N = getN(agent);
        int A = getA(agent);
        return matrix.getValue(N, A);
    }

    public double avgNPV(int year) {
        NPVMatrix matrix = npData.get(year);
        return matrix.averageValue();
    }

    protected static int getN(ConsumerAgent agent) {
        return getIntValue(agent, RAConstants.SLOPE);
    }

    protected static int getA(ConsumerAgent agent) {
        return getIntValue(agent, RAConstants.ORIENTATION);
    }

    protected static int getIntValue(ConsumerAgent agent, String attrName) {
        Attribute<?> attr = agent.findAttribute(attrName);
        if(attr == null) {
            throw new NoSuchElementException("missing argument '" + attrName + "'");
        }
        return (int) AttributeUtil.getDoubleValue(attr, () -> "attribute '" + attrName + "' is no number");
    }

    public double a() {
        return a;
    }

    public double b() {
        return b;
    }

    public double c() {
        return c;
    }

    public double d() {
        return d;
    }

    public int getAdopterPoints() {
        return adopterPoints;
    }

    public int getInterestedPoints() {
        return interestedPoints;
    }

    public int getAwarePoints() {
        return awarePoints;
    }

    public int getUnknownPoints() {
        return unknownPoints;
    }
}
