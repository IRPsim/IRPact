package de.unileipzig.irpact.core.process.ra.npv;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public final class NPVDataSupplier {

    private AttributeHelper attributeHelper;
    private Map<Integer, NPVMatrix> npData;
    private MutableDouble avgFT = new MutableDouble(Double.NaN);
    private Map<Integer, Double> avgNPVCache = new ConcurrentHashMap<>();

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

    private double getNPV(ConsumerAgent agent) {
        return attributeHelper.getDoubleValue(agent, RAConstants.NET_PRESENT_VALUE);
    }

    private int getN(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.SLOPE);
    }

    private int getA(ConsumerAgent agent) {
        return attributeHelper.findIntValue(agent, RAConstants.ORIENTATION);
    }

    private double getFinancialPurchasePower(ConsumerAgent agent) {
        return RAProcessModel.getFinancialPurchasePower(agent, attributeHelper);
    }

    public double getAverageFinancialPurchasePower(Stream<? extends ConsumerAgent> agents) {
        if(Double.isNaN(avgFT.get())) {
            calcAvgFT(agents);
        }
        return avgFT.get();
    }

    private synchronized void calcAvgFT(Stream<? extends ConsumerAgent> agents) {
        if(Double.isNaN(avgFT.get())) {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return getFinancialPurchasePower(ca);
            }).sum();
            double result = sum / total.get();
            avgFT.set(result);
        }
    }

    public double avgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        return cachedAvgNPV(agents, year);
    }

    private double cachedAvgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        Double avgNPV = avgNPVCache.get(year);
        if(avgNPV == null) {
            //return calcAvgNPV(agents, year);
            //return calcAvgNPV2(agents, year);
            return calcGlobalAvgNPV(year);
        } else {
            return avgNPV;
        }
    }

    private synchronized double calcAvgNPV(Stream<? extends ConsumerAgent> agents, final int year) {
        if(avgNPVCache.containsKey(year)) {
            return avgNPVCache.get(year);
        } else {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return NPV(ca, year);
            }).sum();
            double result = sum / total.get();
            avgNPVCache.put(year, result);
            return result;
        }
    }

    private synchronized double calcAvgNPV(final int year) {
        if(avgNPVCache.containsKey(year)) {
            return avgNPVCache.get(year);
        } else {
            NPVMatrix matrix = npData.get(year);
            if(matrix == null) {
                throw new NoSuchElementException("missing npv data for year: " + year);
            }
            double result = matrix.averageValue();
            avgNPVCache.put(year, result);
            return result;
        }
    }

    private synchronized double calcGlobalAvgNPV(int year) {
        if(avgNPVCache.containsKey(year)) {
            return avgNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }
            double total = npData.values()
                    .stream()
                    .mapToDouble(NPVMatrix::averageValue)
                    .sum();
            double result = total / npData.size();
            avgNPVCache.put(year, result);
            return result;
        }
    }
}
