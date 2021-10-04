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

    private Map<Integer, Double> annualAgentAvgNPVCache = new ConcurrentHashMap<>();
    public double avgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        return cachedAnnualAgentAvgNPV(agents, year);
    }
    private double cachedAnnualAgentAvgNPV(Stream<? extends ConsumerAgent> agents, int year) {
        Double avgNPV = annualAgentAvgNPVCache.get(year);
        if(avgNPV == null) {
            return calcAnnualAgentAvgNPV(agents, year);
        } else {
            return avgNPV;
        }
    }
    private synchronized double calcAnnualAgentAvgNPV(Stream<? extends ConsumerAgent> agents, final int year) {
        if(annualAgentAvgNPVCache.containsKey(year)) {
            return annualAgentAvgNPVCache.get(year);
        } else {
            MutableDouble total = MutableDouble.zero();
            double sum = agents.mapToDouble(ca -> {
                total.inc();
                return NPV(ca, year);
            }).sum();
            double result = sum / total.get();
            annualAgentAvgNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualAvgNPVCache = new ConcurrentHashMap<>();
    public double annualAvgNPV(int year) {
        return cachedAnnualAvgNPV(year);
    }
    private double cachedAnnualAvgNPV(int year) {
        Double avgNPV = annualAvgNPVCache.get(year);
        if(avgNPV == null) {
            return calcAnnualAvgNPV(year);
        } else {
            return avgNPV;
        }
    }
    private synchronized double calcAnnualAvgNPV(final int year) {
        if(annualAvgNPVCache.containsKey(year)) {
            return annualAvgNPVCache.get(year);
        } else {
            NPVMatrix matrix = npData.get(year);
            if(matrix == null) {
                throw new NoSuchElementException("missing npv data for year: " + year);
            }
            double result = matrix.averageValue();
            annualAvgNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> globalAvgNPVCache = new ConcurrentHashMap<>();
    public double globalAvgNPV(int year) {
        return cachedGlobalAvgNPV(year);
    }
    private double cachedGlobalAvgNPV(int year) {
        Double avgNPV = globalAvgNPVCache.get(year);
        if(avgNPV == null) {
            return calcGlobalAvgNPV(year);
        } else {
            return avgNPV;
        }
    }
    private synchronized double calcGlobalAvgNPV(int year) {
        if(globalAvgNPVCache.containsKey(year)) {
            return globalAvgNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }
            double total = npData.values()
                    .stream()
                    .mapToDouble(NPVMatrix::averageValue)
                    .sum();
            double result = total / npData.size();
            globalAvgNPVCache.put(year, result);
            return result;
        }
    }
}
