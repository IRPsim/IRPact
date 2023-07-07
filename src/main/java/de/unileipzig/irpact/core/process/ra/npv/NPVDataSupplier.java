package de.unileipzig.irpact.core.process.ra.npv;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AttributeHelper;

import de.unileipzig.irptools.util.log.IRPLogger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
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

    public double NPV(int N, int A, int year) {
        NPVMatrix matrix = npData.get(year);
        return matrix.getValue(N, A);
    }

    private double getNPV(ConsumerAgent agent) {
        return attributeHelper.getDouble(agent, RAConstants.NET_PRESENT_VALUE, true);
    }

    private int getN(ConsumerAgent agent) {
        return attributeHelper.getInt(agent, RAConstants.SLOPE, true);
    }

    private int getA(ConsumerAgent agent) {
        return attributeHelper.getInt(agent, RAConstants.ORIENTATION, true);
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

//    private Map<Integer, Double> annualAgentAvgNPVCache = new ConcurrentHashMap<>();
//    @Deprecated
//    public double annualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, int year) {
//        return cachedAnnualAvgAgentNPV(agents, year);
//    }
//    @Deprecated
//    private double cachedAnnualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, int year) {
//        Double avgNPV = annualAgentAvgNPVCache.get(year);
//        if(avgNPV == null) {
//            return calcAnnualAvgAgentNPV(agents, year);
//        } else {
//            return avgNPV;
//        }
//    }
//    @Deprecated
//    private synchronized double calcAnnualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, final int year) {
//        if(annualAgentAvgNPVCache.containsKey(year)) {
//            return annualAgentAvgNPVCache.get(year);
//        } else {
//            MutableDouble total = MutableDouble.zero();
//            double sum = agents.mapToDouble(ca -> {
//                total.inc();
//                return NPV(ca, year);
//            }).sum();
//            double result = sum / total.get();
//            annualAgentAvgNPVCache.put(year, result);
//            return result;
//        }
//    }
//
//    private Map<Integer, Double> annualAvgNPVCache = new ConcurrentHashMap<>();
//    @Deprecated
//    public double annualAvgNPV(int year) {
//        return cachedAnnualAvgNPV(year);
//    }
//    @Deprecated
//    private double cachedAnnualAvgNPV(int year) {
//        Double avgNPV = annualAvgNPVCache.get(year);
//        if(avgNPV == null) {
//            return calcAnnualAvgNPV(year);
//        } else {
//            return avgNPV;
//        }
//    }
//    @Deprecated
//    private synchronized double calcAnnualAvgNPV(final int year) {
//        if(annualAvgNPVCache.containsKey(year)) {
//            return annualAvgNPVCache.get(year);
//        } else {
//            NPVMatrix matrix = npData.get(year);
//            if(matrix == null) {
//                throw new NoSuchElementException("missing npv data for year: " + year);
//            }
//            double result = matrix.averageValue();
//            annualAvgNPVCache.put(year, result);
//            return result;
//        }
//    }
//
//    private Map<Integer, Double> globalAvgNPVCache = new ConcurrentHashMap<>();
//    @Deprecated
//    public double globalAvgNPV(int year) {
//        return cachedGlobalAvgNPV(year);
//    }
//    @Deprecated
//    private double cachedGlobalAvgNPV(int year) {
//        Double avgNPV = globalAvgNPVCache.get(year);
//        if(avgNPV == null) {
//            return calcGlobalAvgNPV(year);
//        } else {
//            return avgNPV;
//        }
//    }
//    @Deprecated
//    private synchronized double calcGlobalAvgNPV(int year) {
//        if(globalAvgNPVCache.containsKey(1)) {
//            return globalAvgNPVCache.get(1);
//        } else {
//            if(npData.isEmpty()) {
//                throw new NoSuchElementException("missing npv data");
//            }
//            double total = npData.values()
//                .stream()
//                .mapToDouble(NPVMatrix::averageValue)
//                .sum();
//            double result = total / npData.size();
//            globalAvgNPVCache.put(1, result);
//            return result;
//        }
//    }
//    @Deprecated
//    private synchronized double calcGlobalAvgNPV2(int start, int end) {
//        if(globalAvgNPVCache.containsKey(0)) {
//            return globalAvgNPVCache.get(0);
//        } else {
//            if(npData.isEmpty()) {
//                throw new NoSuchElementException("missing npv data");
//            }
//            double total = 0;
//            double count = 0;
//            for (int y = start; y <= end; y++) {
//                total += npData.get(y).averageValue();
//                count++;
//            }
//            double result = total / count;
//            globalAvgNPVCache.put(0, result);
//            return result;
//        }
//    }
//    @Deprecated
//    private synchronized double calcGlobalAvgAgentNPV2(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
//        if(globalAvgNPVCache.containsKey(3)) {
//            return globalAvgNPVCache.get(3);
//        } else {
//            if(npData.isEmpty()) {
//                throw new NoSuchElementException("missing npv data");
//            }
//
//            double result = 0.0;
//            double total = 0;
//            for (int year = start; year <= end; year++) {
//                double annual = annualAvgAgentNPV(supplier.get(), year);
//                result += annual;
//                total += 1;
//            }
//            result /= total;
//            globalAvgNPVCache.put(3, result);
//            return result;
//        }
//    }

    //=========================
    // NEW
    //=========================

    public void logInfo(IRPLogger logger, SimulationEnvironment environment) {
        logger.info("NPV INFORMATION");
        logger.info("start={} end={}", environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
        logger.info("CONSUMER AGENT OVERVIEW");
        logger.info("agent N A NPVs");
        environment.getAgents().streamConsumerAgents().forEach(agent -> {
            int N = getN(agent);
            int A = getA(agent);
            double[] npvs = new double[environment.getSettings().getNumberOfSimulationYears()];
            for (int year = environment.getSettings().getFirstSimulationYear(), j = 0; year <= environment.getSettings().getLastSimulationYear(); year++, j++) {
                npvs[j] = NPV(agent, year);
            }
            logger.info("{} {} {} {}", agent.getName(), N, A, Arrays.toString(npvs));
        });
        logger.info("ANNUAL STATICS OVERVIEW");
        logger.info("year existingAnnualAsset allAnnualAsset annualAgent");
        for (int year = environment.getSettings().getFirstSimulationYear(); year <= environment.getSettings().getLastSimulationYear(); year++) {
            double existingAnnualAsset = annualAvgExistingAssetNPV(environment.getAgents().streamConsumerAgents(), year);
            double allAnnualAsset = annualAvgAssetNPV(year);
            double annualAgent = annualAvgAgentNPV(environment.getAgents().streamConsumerAgents(), year);
            logger.info("{} {} {} {}", year, existingAnnualAsset, allAnnualAsset, annualAgent);
        }
        logger.info("GLOBAL STATICS OVERVIEW");
        double existingGlobalAsset = globalAvgExistingAssetNPV(() -> environment.getAgents().streamConsumerAgents(), environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
        double allGlobalAsset = globalAvgAssetNPV(environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
        double globalAgent = globalAvgAgentNPV(() -> environment.getAgents().streamConsumerAgents(), environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
        logger.info("existingGlobalAsset {}", existingGlobalAsset);
        logger.info("allGlobalAsset {}", allGlobalAsset);
        logger.info("globalAgent {}", globalAgent);
    }

    private Map<Integer, Double> annualAvgAgentNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, final int year) {
        if(annualAvgAgentNPVCache.containsKey(year)) {
            return annualAvgAgentNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble sum = MutableDouble.zero();
            MutableDouble total = MutableDouble.zero();
            agents.mapToDouble(agent -> NPV(agent, year))
                .forEach(npv -> {
                    total.inc();
                    sum.add(npv);
                });
            double result = sum.get() / total.get();
            annualAvgAgentNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualMinAgentNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMinAgentNPV(Stream<? extends ConsumerAgent> agents, final int year) {
        if(annualMinAgentNPVCache.containsKey(year)) {
            return annualMinAgentNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble min = MutableDouble.empty();
            agents.mapToDouble(agent -> NPV(agent, year))
                .forEach(min::setMin);
            double result = min.doubleValue();
            annualMinAgentNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualMaxAgentNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMaxAgentNPV(Stream<? extends ConsumerAgent> agents, final int year) {
        if(annualMaxAgentNPVCache.containsKey(year)) {
            return annualMaxAgentNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble max = MutableDouble.empty();
            agents.mapToDouble(agent -> NPV(agent, year))
                .forEach(max::setMax);
            double result = max.doubleValue();
            annualMaxAgentNPVCache.put(year, result);
            return result;
        }
    }

    private MutableDouble globalAvgAgentNPV2Cache;
    public synchronized double globalAvgAgentNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalAvgAgentNPV2Cache == null) {
            double sum = 0.0;
            double total = 0.0;
            for (int year = start; year <= end; year++) {
                sum += annualAvgAgentNPV(supplier.get(), year);
                total += 1;
            }
            double result = sum / total;
            globalAvgAgentNPV2Cache = new MutableDouble(result);
        }
        return globalAvgAgentNPV2Cache.get();
    }

    private MutableDouble globalMaxAgentNPV2Cache;
    public synchronized double globalMaxAgentNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalMaxAgentNPV2Cache == null) {
            double max = Double.NaN;
            for (int year = start; year <= end; year++) {
                double annualMax = annualMaxAgentNPV(supplier.get(), year);
                if (Double.isNaN(max)) {
                    max = annualMax;
                } else {
                    max = Math.max(max, annualMax);
                }
            }
            if (Double.isNaN(max)) {
                throw new IllegalStateException("max is NaN");
            }
            globalMaxAgentNPV2Cache = new MutableDouble(max);
        }
        return globalMaxAgentNPV2Cache.get();
    }

    private MutableDouble globalMinAgentNPV2Cache;
    public synchronized double globalMinAgentNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalMinAgentNPV2Cache == null) {
            double min = Double.NaN;
            for (int year = start; year <= end; year++) {
                double annualMin = annualMinAgentNPV(supplier.get(), year);
                if (Double.isNaN(min)) {
                    min = annualMin;
                } else {
                    min = Math.min(min, annualMin);
                }
            }
            if (Double.isNaN(min)) {
                throw new IllegalStateException("min is NaN");
            }
            globalMinAgentNPV2Cache = new MutableDouble(min);
        }
        return globalMinAgentNPV2Cache.get();
    }

    private Map<Integer, Double> annualAvgAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualAvgAssetNPV(int year) {
        if (annualAvgAssetNPVCache.containsKey(year)) {
            return annualAvgAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            double sum = 0.0;
            double total = 0.0;

            NPVMatrix matrix = npData.get(year);
            for (int N = 0; N < matrix.getNmax(); N++) {
                for (int A = 0; A < matrix.getAmax(); A++) {
                    sum += matrix.getValue(N, A);
                    total += 1;
                }
            }

            double result = sum / total;
            annualAvgAssetNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualMinAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMinAssetNPV(int year) {
        if (annualMinAssetNPVCache.containsKey(year)) {
            return annualMinAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            double min = Double.NaN;

            NPVMatrix matrix = npData.get(year);
            for (int N = 0; N < matrix.getNmax(); N++) {
                for (int A = 0; A < matrix.getAmax(); A++) {
                    double current = matrix.getValue(N, A);
                    if (Double.isNaN(min)) {
                        min = current;
                    } else {
                        min = Math.min(min, current);
                    }
                }
            }

            annualMinAssetNPVCache.put(year, min);
            return min;
        }
    }

    private Map<Integer, Double> annualMaxAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMaxAssetNPV(int year) {
        if (annualMaxAssetNPVCache.containsKey(year)) {
            return annualMaxAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            double max = Double.NaN;

            NPVMatrix matrix = npData.get(year);
            for (int N = 0; N < matrix.getNmax(); N++) {
                for (int A = 0; A < matrix.getAmax(); A++) {
                    double current = matrix.getValue(N, A);
                    if (Double.isNaN(max)) {
                        max = current;
                    } else {
                        max = Math.max(max, current);
                    }
                }
            }

            annualMaxAssetNPVCache.put(year, max);
            return max;
        }
    }

    private MutableDouble globalAvgAssetNPVCache;
    public synchronized double globalAvgAssetNPV(int start, int end) {
        if (globalAvgAssetNPVCache == null) {
            double sum = 0.0;
            double total = 0.0;
            for (int year = start; year <= end; year++) {
                sum += annualAvgAssetNPV(year);
                total += 1;
            }
            double result = sum / total;
            globalAvgAssetNPVCache = new MutableDouble(result);
        }
        return globalAvgAssetNPVCache.get();
    }

    private MutableDouble globalMaxAssetNPVCache;
    public synchronized double globalMaxAssetNPV(int start, int end) {
        if (globalMaxAssetNPVCache == null) {
            double max = Double.NaN;
            for (int year = start; year <= end; year++) {
                double current = annualMaxAssetNPV(year);
                if (Double.isNaN(max)) {
                    max = current;
                } else {
                    max = Math.max(max, current);
                }
            }
            globalMaxAssetNPVCache = new MutableDouble(max);
        }
        return globalMaxAssetNPVCache.get();
    }

    private MutableDouble globalMinAssetNPVCache;
    public synchronized double globalMinAssetNPV(int start, int end) {
        if (globalMinAssetNPVCache == null) {
            double min = Double.NaN;
            for (int year = start; year <= end; year++) {
                double current = annualMinAssetNPV(year);
                if (Double.isNaN(min)) {
                    min = current;
                } else {
                    min = Math.min(min, current);
                }
            }
            globalMinAssetNPVCache = new MutableDouble(min);
        }
        return globalMinAssetNPVCache.get();
    }

    private Map<Integer, Double> annualAvgExistingAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualAvgExistingAssetNPV(Stream<? extends ConsumerAgent> agents, int year) {
        if (annualAvgExistingAssetNPVCache.containsKey(year)) {
            return annualAvgExistingAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble sum = MutableDouble.zero();
            MutableDouble total = MutableDouble.zero();
            agents.map(agent -> Pair.get(getN(agent), getA(agent)))
                .distinct()
                .mapToDouble(pair -> NPV(pair.first(), pair.second(), year))
                .forEach(npv -> {
                    total.inc();
                    sum.add(npv);
                });
            double result = sum.get() / total.get();
            annualAvgExistingAssetNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualMinExistingAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMinExistingAssetNPV(Stream<? extends ConsumerAgent> agents, int year) {
        if (annualMinExistingAssetNPVCache.containsKey(year)) {
            return annualMinExistingAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble min = MutableDouble.empty();
            agents.map(agent -> Pair.get(getN(agent), getA(agent)))
                .distinct()
                .mapToDouble(pair -> NPV(pair.first(), pair.second(), year))
                .forEach(min::setMin);
            double result = min.get();
            annualMinExistingAssetNPVCache.put(year, result);
            return result;
        }
    }

    private Map<Integer, Double> annualMaxExistingAssetNPVCache = new ConcurrentHashMap<>();
    public synchronized double annualMaxExistingAssetNPV(Stream<? extends ConsumerAgent> agents, int year) {
        if (annualMaxExistingAssetNPVCache.containsKey(year)) {
            return annualMaxExistingAssetNPVCache.get(year);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            MutableDouble min = MutableDouble.empty();
            agents.map(agent -> Pair.get(getN(agent), getA(agent)))
                .distinct()
                .mapToDouble(pair -> NPV(pair.first(), pair.second(), year))
                .forEach(min::setMax);
            double result = min.get();
            annualMaxExistingAssetNPVCache.put(year, result);
            return result;
        }
    }

    private MutableDouble globalAvgExistingAssetNPVCache;
    public synchronized double globalAvgExistingAssetNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalAvgExistingAssetNPVCache == null) {
            double sum = 0.0;
            double total = 0.0;
            for (int year = start; year <= end; year++) {
                sum += annualAvgExistingAssetNPV(supplier.get(), year);
                total += 1;
            }
            double result = sum / total;
            globalAvgExistingAssetNPVCache = new MutableDouble(result);
        }
        return globalAvgExistingAssetNPVCache.get();
    }

    private MutableDouble globalMaxExistingAssetNPVCache;
    public synchronized double globalMaxExistingAssetNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalMaxExistingAssetNPVCache == null) {
            double max = Double.NaN;
            for (int year = start; year <= end; year++) {
                double current = annualMaxExistingAssetNPV(supplier.get(), year);
                if (Double.isNaN(max)) {
                    max = current;
                } else {
                    max = Math.max(max, current);
                }
            }
            globalMaxExistingAssetNPVCache = new MutableDouble(max);
        }
        return globalMaxExistingAssetNPVCache.get();
    }

    private MutableDouble globalMinExistingAssetNPVCache;
    public synchronized double globalMinExistingAssetNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if (globalMinExistingAssetNPVCache == null) {
            double min = Double.NaN;
            for (int year = start; year <= end; year++) {
                double current = annualMinExistingAssetNPV(supplier.get(), year);
                if (Double.isNaN(min)) {
                    min = current;
                } else {
                    min = Math.min(min, current);
                }
            }
            globalMinExistingAssetNPVCache = new MutableDouble(min);
        }
        return globalMinExistingAssetNPVCache.get();
    }
}
