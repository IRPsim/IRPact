package de.unileipzig.irpact.core.process.ra.npv;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class NPVDataSupplier {

    protected AttributeHelper attributeHelper;
    protected Map<Integer, NPVMatrix> npData;
    protected MutableDouble avgFT = new MutableDouble(Double.NaN);

    public NPVDataSupplier() {
        this(null, new HashMap<>());
    }

    public NPVDataSupplier(AttributeHelper attributeHelper) {
        this(attributeHelper, new HashMap<>());
    }

    public NPVDataSupplier(Map<Integer, NPVMatrix> npData) {
        this(null, npData);
    }

    public NPVDataSupplier(AttributeHelper attributeHelper, Map<Integer, NPVMatrix> npData) {
        this.attributeHelper = attributeHelper;
        this.npData = npData;
    }

    public void setAttributeHelper(AttributeHelper attributeHelper) {
        this.attributeHelper = attributeHelper;
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

    protected double getNPV(ConsumerAgent agent) {
        return attributeHelper.getDoubleValue(agent, RAConstants.NET_PRESENT_VALUE);
    }

    protected int getN(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.SLOPE);
    }

    protected int getA(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.ORIENTATION);
    }

    protected double getFinancialPurchasePower(ConsumerAgent agent) {
        return RAProcessModel.getFinancialPurchasePower(agent, attributeHelper);
    }

    public double getAverageFinancialPurchasePower(Stream<? extends ConsumerAgent> agents) {
        if(Double.isNaN(avgFT.get())) {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return getFinancialPurchasePower(ca);
            }).sum();
            double result = sum / total.get();
            avgFT.set(result);
        }
        return avgFT.get();
    }
}
