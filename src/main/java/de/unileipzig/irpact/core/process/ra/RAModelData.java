package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.spatial.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class RAModelData {

    protected Map<Integer, NPVMatrix> npData;

    protected double a;
    protected double b;
    protected double c;
    protected double d;

    protected int adopterPoints = 3;
    protected int interesetedPoints = 2;
    protected int awarePoints = 1;
    protected int unknownPoints = 0;

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

    public void setInteresetedPoints(int interesetedPoints) {
        this.interesetedPoints = interesetedPoints;
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
        SpatialInformation information = agent.getSpatialInformation();
        SpatialAttribute sAttr = information.getAttribute(RAConstants.SLOPE);
        if(sAttr != null) {
            return (int) sAttr.getDoubleValue();
        } else {
            ConsumerAgentAttribute caAttr = agent.getAttribute(RAConstants.SLOPE);
            return (int) caAttr.getDoubleValue();
        }
    }

    protected static int getA(ConsumerAgent agent) {
        SpatialInformation information = agent.getSpatialInformation();
        SpatialAttribute sAttr = information.getAttribute(RAConstants.ORIENTATION);
        if(sAttr != null) {
            return (int) sAttr.getDoubleValue();
        } else {
            ConsumerAgentAttribute caAttr = agent.getAttribute(RAConstants.ORIENTATION);
            return (int) caAttr.getDoubleValue();
        }
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

    public int getInteresetedPoints() {
        return interesetedPoints;
    }

    public int getAwarePoints() {
        return awarePoints;
    }

    public int getUnknownPoints() {
        return unknownPoints;
    }
}
