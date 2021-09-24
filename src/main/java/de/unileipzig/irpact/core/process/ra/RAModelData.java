package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
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

    protected double aWeight;
    protected double bWeight;
    protected double cWeight;
    protected double dWeight;

    protected int adopterPoints = DEFAULT_ADOPTER_POINTS;
    protected int interestedPoints = DEFAULT_INTERESTED_POINTS;
    protected int awarePoints = DEFAULT_AWARE_POINTS;
    protected int unknownPoints = DEFAULT_UNKNOWN_POINTS;

    protected double weightFT = 0.5;
    protected double weightNPV = 0.5;
    protected double weightSocial = 0.5;
    protected double weightLocal = 0.5;

    protected double logisticFactor = 0.0;

    protected double communicationFactor = 1.0;
    protected double rewireFactor = 1.0;

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

    public void setAWeight(double aWeight) {
        this.aWeight = aWeight;
    }

    public void setBWeight(double bWeight) {
        this.bWeight = bWeight;
    }

    public void setCWeight(double cWeight) {
        this.cWeight = cWeight;
    }

    public void setDWeight(double dWeight) {
        this.dWeight = dWeight;
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

    public void setCommunicationFactor(double communicationFactor) {
        this.communicationFactor = communicationFactor;
    }

    public void setRewireFactor(double rewireFactor) {
        this.rewireFactor = rewireFactor;
    }

    public void put(int year, NPVMatrix matrix) {
        npvDataSupplier.put(year, matrix);
    }

    public double NPV(ConsumerAgent agent, int year) {
        return npvDataSupplier.NPV(agent, year);
    }

    public double avgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        return npvDataSupplier.avgNPV(agents, year);
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

    public double getAWeight() {
        return aWeight;
    }

    public double getBWeight() {
        return bWeight;
    }

    public double getCWeight() {
        return cWeight;
    }

    public double getDWeight() {
        return dWeight;
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

    public double getCommunicationFactor() {
        return communicationFactor;
    }

    public double getRewireFactor() {
        return rewireFactor;
    }
}
