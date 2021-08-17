package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
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

    protected final NPVDataSupplier npvDataSupplier;

    protected double a;
    protected double b;
    protected double c;
    protected double d;

    protected int adopterPoints = DEFAULT_ADOPTER_POINTS;
    protected int interestedPoints = DEFAULT_INTERESTED_POINTS;
    protected int awarePoints = DEFAULT_AWARE_POINTS;
    protected int unknownPoints = DEFAULT_UNKNOWN_POINTS;

    protected double weightFT = 0.5;
    protected double weightNPV = 0.5;
    protected double weightSocial = 0.5;
    protected double weightLocal = 0.5;

    protected double logisticFactor = 0.0;

    public RAModelData() {
        this(new HashMap<>());
    }

    public RAModelData(Map<Integer, NPVMatrix> npData) {
        npvDataSupplier = new NPVDataSupplier(npData);
    }

    public void setAttributeHelper(AttributeHelper attributeHelper) {
        npvDataSupplier.setAttributeHelper(attributeHelper);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                a, b, c, d,
                adopterPoints, interestedPoints, awarePoints, unknownPoints,
                logisticFactor,
                weightFT, weightNPV, weightSocial, weightLocal
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

    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }

    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }

    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }

    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }

    public void put(int year, NPVMatrix matrix) {
        npvDataSupplier.put(year, matrix);
    }

    public double NPV(ConsumerAgent agent, int year) {
        return npvDataSupplier.NPV(agent, year);
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

    public double getAverageFinancialPurchasePower(Stream<? extends ConsumerAgent> agents) {
        return npvDataSupplier.getAverageFinancialPurchasePower(agents);
    }

    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
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

    public double getWeightFT() {
        return weightFT;
    }

    public double getWeightNPV() {
        return weightNPV;
    }

    public double getWeightSocial() {
        return weightSocial;
    }

    public double getWeightLocal() {
        return weightLocal;
    }
}
