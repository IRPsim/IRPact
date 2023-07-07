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
public final class AssetNPVDataSupplier {

    private Map<Pair<Integer, Integer>, Integer> assetCounts = new HashMap<>();
    private int totalAssets = 0;
    private AttributeHelper attributeHelper;
    private Map<Integer, NPVMatrix> npData;
    private MutableDouble avgFT = new MutableDouble(Double.NaN);

    public AssetNPVDataSupplier() {
        this(null, new HashMap<>());
    }

    public AssetNPVDataSupplier(AttributeHelper attributeHelper) {
        this(attributeHelper, new HashMap<>());
    }

    public AssetNPVDataSupplier(Map<Integer, NPVMatrix> npData) {
        this(null, npData);
    }

    public AssetNPVDataSupplier(AttributeHelper attributeHelper, Map<Integer, NPVMatrix> npData) {
        this.attributeHelper = attributeHelper;
        this.npData = npData;
    }

    public void setAttributeHelper(AttributeHelper attributeHelper) {
        this.attributeHelper = attributeHelper;
    }

    public void put(int year, NPVMatrix matrix) {
        npData.put(year, matrix);
    }

    public void addAssets(Stream<? extends ConsumerAgent> agents) {
        agents.forEach(this::addAsset);
    }

    public void addAsset(ConsumerAgent agent) {
        addAsset(getN(agent), getA(agent));
    }

    public void addAsset(int N, int A) {
        Pair<Integer, Integer> asset = Pair.get(N, A);
        int current = assetCounts.getOrDefault(asset, 0);
        assetCounts.put(asset, current + 1);
        totalAssets++;
    }

    public double NPV(ConsumerAgent agent, int year) {
        NPVMatrix matrix = npData.get(year);
        if(matrix == null) {
            return getAssetWeight(agent) * getNPV(agent);
        } else {
            return NPV(agent, matrix);
        }
    }

    public double NPV(ConsumerAgent agent, NPVMatrix matrix) {
        double weight = getAssetWeight(agent);
        int N = getN(agent);
        int A = getA(agent);
        return weight * matrix.getValue(N, A);
    }

    private double getAssetWeight(ConsumerAgent agent) {
        return getAssetWeight(getN(agent), getA(agent));
    }

    private double getAssetWeight(int N, int A) {
        Pair<Integer, Integer> asset = Pair.get(N, A);
        int count = assetCounts.get(asset);
        return (double) count / (double) totalAssets;
    }

    private double getNPV(ConsumerAgent agent) {
        return attributeHelper.getDouble(agent, RAConstants.NET_PRESENT_VALUE, true);
    }

    public void logInfo(IRPLogger logger, SimulationEnvironment environment) {
        logger.info("NPV INFORMATION");
        int totalCount = assetCounts.values().stream().mapToInt(Integer::intValue).sum();
        logger.info("sanity check: {} =? {} =? {}", totalCount, totalAssets, environment.getAgents().streamConsumerAgents().count());
        logger.info("start={} end={}", environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
        logger.info("CONSUMER AGENT OVERVIEW");
        logger.info("agent N A weight npvs weightedNpvs");
        environment.getAgents().streamConsumerAgents().forEach(agent -> {
            int N = getN(agent);
            int A = getA(agent);
            double weight = getAssetWeight(agent);
            double[] npvs = new double[environment.getSettings().getNumberOfSimulationYears()];
            double[] weightedNpvs = new double[npvs.length];
            for (int year = environment.getSettings().getFirstSimulationYear(), j = 0; year <= environment.getSettings().getLastSimulationYear(); year++, j++) {
                npvs[j] = npData.get(year).getValue(N, A);
                weightedNpvs[j] = NPV(agent, year);
            }
            logger.info("{} {} {} {} {} {}", agent.getName(), N, A, weight, Arrays.toString(npvs), Arrays.toString(weightedNpvs));
        });
        logger.info("STATICS OVERVIEW");
        logger.info("year annual global global2");
        for (int year = environment.getSettings().getFirstSimulationYear(); year <= environment.getSettings().getLastSimulationYear(); year++) {
            double annual = annualAvgAgentNPV(environment.getAgents().streamConsumerAgents(), year);
            double global = calcGlobalAvgNPV(() -> environment.getAgents().streamConsumerAgents(), year);
            double global2 = calcGlobalAvgNPV2(() -> environment.getAgents().streamConsumerAgents(), environment.getSettings().getFirstSimulationYear(), environment.getSettings().getLastSimulationYear());
            logger.info("{} {} {} {}", year, annual, global, global2);
        }
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

    private Map<Integer, Double> annualAgentAvgNPVCache = new ConcurrentHashMap<>();
    public double annualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, int year) {
        return cachedAnnualAvgAgentNPV(agents, year);
    }
    private double cachedAnnualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, int year) {
        Double avgNPV = annualAgentAvgNPVCache.get(year);
        if(avgNPV == null) {
            return calcAnnualAvgAgentNPV(agents, year);
        } else {
            return avgNPV;
        }
    }
    private synchronized double calcAnnualAvgAgentNPV(Stream<? extends ConsumerAgent> agents, final int year) {
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

    private Map<Integer, Double> globalAvgNPVCache = new ConcurrentHashMap<>();
    public double globalAvgNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int year) {
        return cachedGlobalAvgNPV(supplier, year);
    }
    private double cachedGlobalAvgNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int year) {
        Double avgNPV = globalAvgNPVCache.get(year);
        if(avgNPV == null) {
            return calcGlobalAvgNPV(supplier, year);
        } else {
            return avgNPV;
        }
    }
    private synchronized double calcGlobalAvgNPV(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int year) {
        if(globalAvgNPVCache.containsKey(0)) {
            return globalAvgNPVCache.get(0);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            ConsumerAgent first = supplier.get().findAny().get();
            SimulationEnvironment environment = first.getEnvironment();
            int startYear = environment.getSettings().getFirstSimulationYear();
            int endYear = environment.getSettings().getLastSimulationYear();

            double totalAgents = (double) supplier.get().count();
            double result = 0.0;
            double total = 0;

            for (int y = startYear; y <= endYear; y++) {
                NPVMatrix matrix = npData.get(y);
                total++;
                double annualTotal = supplier.get().mapToDouble(agent -> NPV(agent, matrix)).sum();
                double annual = (annualTotal / totalAgents);
                result += annual;
            }
            result /= total;
            globalAvgNPVCache.put(0, result);
            return result;
        }
    }
    private synchronized double calcGlobalAvgNPV2(Supplier<? extends Stream<? extends ConsumerAgent>> supplier, int start, int end) {
        if(globalAvgNPVCache.containsKey(1)) {
            return globalAvgNPVCache.get(1);
        } else {
            if(npData.isEmpty()) {
                throw new NoSuchElementException("missing npv data");
            }

            double result = 0.0;
            double total = 0;
            for (int year = start; year <= end; year++) {
                double annual = annualAvgAgentNPV(supplier.get(), year);
                result += annual;
                total += 1;
            }
            result /= total;
            globalAvgNPVCache.put(1, result);
            return result;
        }
    }
}
