package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class RAModelData implements ChecksumComparable {

    public static final int DEFAULT_ADOPTER_POINTS = 3;
    public static final int DEFAULT_INTERESTED_POINTS = 2;
    public static final int DEFAULT_AWARE_POINTS = 1;
    public static final int DEFAULT_UNKNOWN_POINTS = 0;

    protected AttributeHelper attributeHelper;

    protected Map<Integer, NPVMatrix> npData;

    protected double a;
    protected double b;
    protected double c;
    protected double d;

    protected int adopterPoints = DEFAULT_ADOPTER_POINTS;
    protected int interestedPoints = DEFAULT_INTERESTED_POINTS;
    protected int awarePoints = DEFAULT_AWARE_POINTS;
    protected int unknownPoints = DEFAULT_UNKNOWN_POINTS;

    protected double logisticFactor = 0.0;

    public RAModelData() {
        this(new HashMap<>());
    }

    public RAModelData(Map<Integer, NPVMatrix> npData) {
        this.npData = npData;
    }

    public void setAttributeHelper(AttributeHelper attributeHelper) {
        this.attributeHelper = attributeHelper;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                a, b, c, d,
                adopterPoints, interestedPoints, awarePoints, unknownPoints,
                logisticFactor
        );
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
        if(matrix == null) {
            return getNPV(agent);
        } else {
            int N = getN(agent);
            int A = getA(agent);
            return matrix.getValue(N, A);
        }
    }

    protected MutableDouble avgNPV = new MutableDouble(Double.NaN);
    protected int avgNPVYear = -1;

    public double avgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        if(Double.isNaN(avgNPV.get()) || year != avgNPVYear) {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return NPV(ca, year);
            }).sum();
            double result = sum / total.get();
            avgNPV.set(result);
            avgNPVYear = year;
        }
        return avgNPV.get();
    }

    protected MutableDouble avgFT = new MutableDouble(Double.NaN);

    public double getAverageFinancialThresholdAgent(Stream<? extends ConsumerAgent> agents) {
        if(Double.isNaN(avgFT.get())) {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return getFinancialThreshold(ca);
            }).sum();
            double result = sum / total.get();
            avgFT.set(result);
        }
        return avgFT.get();
    }

    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }

    protected double getFinancialThreshold(ConsumerAgent agent) {
        return RAProcessModel.getFinancialThreshold(agent, attributeHelper);
    }

    protected double getNPV(ConsumerAgent agent) {
        return attributeHelper.getDoubleValue(agent, RAConstants.NET_PRESENT_VALUE);
    }

    protected int getN(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.SLOPE);
    }

    protected int getA(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.ORIENTATION);
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

    public double getLogisticFactor() {
        return logisticFactor;
    }
}
