package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Abitz
 */
public class BasicPostAnalysisData implements PostAnalysisData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicPostAnalysisData.class);

    private SimulationEnvironment environment;

    public BasicPostAnalysisData() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    //=========================
    //general
    //=========================

    @Override
    public int getNumberOfInitialAdopter(Product product) {
        int total = 0;
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                for(AdoptedProduct ap: ca.getAdoptedProducts()) {
                    if(ap.isInitial() && ap.isProduct(product)) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private int getStartYear() {
        return environment.getTimeModel().getFirstSimulationYear();
    }

    private int getEndYear() {
        return environment.getTimeModel().getLastSimulationYear();
    }

    //=========================
    //phase transition
    //=========================

    protected final Map<ConsumerAgent, PhaseTransitionList> transitionMap = new ConcurrentHashMap<>();
    protected boolean logPhaseTransition = true;

    protected PhaseTransitionList getInfo(ConsumerAgent agent) {
        return transitionMap.computeIfAbsent(agent, _agent -> new PhaseTransitionList());
    }

    @Override
    public void setLogPhaseTransition(boolean enable) {
        logPhaseTransition = enable;
    }

    @Override
    public boolean isLogPhaseTransition() {
        return logPhaseTransition;
    }

    @Override
    public void logPhaseTransition(ConsumerAgent agent, int phase, Product product, Timestamp stamp) {
        if(logPhaseTransition) {
            getInfo(agent).add(phase, product, stamp);
        }
    }

    @Override
    public Map<Integer, Integer> getTransitionOverviewForYear(Product product, int year) {
        if(logPhaseTransition) {
            Map<Integer, Integer> overviewMap = new TreeMap<>();
            for(PhaseTransitionList transitionList: transitionMap.values()) {
                BasicPhaseTransition info = transitionList.getLatestInfoUntilYear(product, year);
                if(info != null) {
                    int current = overviewMap.getOrDefault(info.getPhase(), 0);
                    overviewMap.put(info.getPhase(), current + 1);
                }
            }
            return overviewMap;
        } else {
            return null;
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class PhaseTransitionList {

        private final Map<Product, List<BasicPhaseTransition>> transitions = new ConcurrentHashMap<>();
        private boolean prepared = false;

        private PhaseTransitionList() {
        }

        private List<BasicPhaseTransition> getList(Product product) {
            return transitions.computeIfAbsent(product, _product -> new ArrayList<>());
        }

        private void prepareForAnalysis() {
            if(prepared) {
                return;
            }

            prepared = true;
            for(List<BasicPhaseTransition> list: transitions.values()) {
                list.sort(BasicPhaseTransition.COMPARE_STAMP);
            }
        }

        private boolean has(int phase, Product product, int year) {
            List<BasicPhaseTransition> list = transitions.get(product);
            if(list == null) return false;

            for(BasicPhaseTransition info: list) {
                if(info.isPhaseProductYear(phase, product, year)) {
                    return true;
                }
            }
            return false;
        }

        private void add(int phase, Product product, Timestamp stamp) {
            getList(product).add(new BasicPhaseTransition(phase, product, stamp));
            prepared = false;
        }

        private BasicPhaseTransition getLatestInfoUntilYear(Product product, int year) {
            prepareForAnalysis();

            List<BasicPhaseTransition> list = transitions.get(product);
            if(list == null) return null;

            BasicPhaseTransition latest = null;
            //infos sorted after year - use last valid entry
            for(BasicPhaseTransition info: list) {
                if(info.getYear() <= year) { //infos sorted after stamp
                    latest = info;
                }
            }
            return latest;
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class BasicPhaseTransition implements PhaseTransition {

        private static final Comparator<BasicPhaseTransition> COMPARE_STAMP = Comparator.comparing(BasicPhaseTransition::getStamp);

        private final int phase;
        private final Product product;
        private final Timestamp stamp;

        private BasicPhaseTransition(int phase, Product product, Timestamp stamp) {
            this.phase = phase;
            this.product = product;
            this.stamp = stamp;
        }

        private boolean isPhaseProductYear(int phase, Product product, int year) {
            return this.phase == phase && isProductYear(product, year);
        }

        private boolean isProductYear(Product product, int year) {
            return this.product == product && getStamp().getYear() == year;
        }

        private int getYear() {
            return stamp.getYear();
        }

        @Override
        public int getPhase() {
            return phase;
        }

        @Override
        public Product getProduct() {
            return product;
        }

        @Override
        public Timestamp getStamp() {
            return stamp;
        }

        @Override
        public int compareTo(PhaseTransition o) {
            return stamp.compareTo(o.getStamp());
        }
    }

    //=========================
    //annual interest
    //=========================

    private final Map<Integer, Map<Product, BasicCumulatedAnnualInterest>> cumulatedAnnualInterest = new ConcurrentHashMap<>();
    private final Map<Product, Map<ConsumerAgent, BasicAnnualInterest>> annualInterest = new ConcurrentHashMap<>();
    protected boolean logAnnualInterest = true;

    @Override
    public void setLogAnnualInterest(boolean logAnnualInterest) {
        this.logAnnualInterest = logAnnualInterest;
    }

    @Override
    public boolean isLogAnnualInterest() {
        return logAnnualInterest;
    }

    private BasicCumulatedAnnualInterest getCumulatedAnnualInterest(Product product, Timestamp stamp) {
        Map<Product, BasicCumulatedAnnualInterest> productMapping = cumulatedAnnualInterest.computeIfAbsent(stamp.getYear(), _year -> new ConcurrentHashMap<>());
        BasicCumulatedAnnualInterest interest = productMapping.get(product);
        if(interest == null) {
            interest = new BasicCumulatedAnnualInterest(stamp.getYear(), product);
            productMapping.put(product, interest);
        }
        return interest;
    }

    private BasicAnnualInterest getAnnualInterest(ConsumerAgent agent, Product product) {
        Map<ConsumerAgent, BasicAnnualInterest> productMapping = annualInterest.computeIfAbsent(product, _product -> new ConcurrentHashMap<>());
        BasicAnnualInterest interest = productMapping.get(agent);
        if(interest == null) {
            interest = new BasicAnnualInterest(product);
            productMapping.put(agent, interest);
        }
        return interest;
    }

    @Override
    public void logAnnualInterest(ConsumerAgent agent, Product product, double interest, Timestamp stamp) {
        if(logAnnualInterest) {
            getCumulatedAnnualInterest(product, stamp).update(interest);
            getAnnualInterest(agent, product).update(stamp, interest);
        }
    }

    @Override
    public CumulatedAnnualInterest getCumulatedAnnualInterest(Product product, int year) {
        Map<Product, BasicCumulatedAnnualInterest> productMapping = cumulatedAnnualInterest.get(year);
        return productMapping == null
                ? null
                : productMapping.get(product);
    }

    @Override
    public int getCumulatedAnnualInterestCount(Product product, int year, double interest) {
        CumulatedAnnualInterest cumulatedAnnualInterest = getCumulatedAnnualInterest(product, year);
        if(cumulatedAnnualInterest == null) {
            LOGGER.warn("no interest for product '{}' in year '{}'", product.getName(), year);
            return 0;
        }
        return cumulatedAnnualInterest.getInterestCount(interest);
    }

    @Override
    public double getAnnualInterest(ConsumerAgent agent, Product product, int year) {
        Map<ConsumerAgent, BasicAnnualInterest> annualMap = annualInterest.get(product);
        if(annualMap == null) return 0;
        BasicAnnualInterest interet = annualMap.get(agent);
        return interet == null
                ? 0
                : interet.getInterest(year);
    }

    /**
     * @author Daniel Abitz
     */
    private static class BasicCumulatedAnnualInterest implements CumulatedAnnualInterest {

        private final int year;
        private final Product product;
        private final Map<Double, Integer> interest = new ConcurrentHashMap<>();

        private BasicCumulatedAnnualInterest(int year, Product product) {
            this.year = year;
            this.product = product;
        }

        private void update(double interestValue) {
            int current = interest.getOrDefault(interestValue, 0);
            interest.put(interestValue, current + 1);
        }

        @Override
        public int getYear() {
            return year;
        }

        @Override
        public Product getProduct() {
            return product;
        }

        @Override
        public Map<Double, Integer> getInterest() {
            return interest;
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class BasicAnnualInterest implements AnnualInterest {

        private final Product product;
        private final Map<Integer, Double> annualInterest = new HashMap<>();

        private BasicAnnualInterest(Product product) {
            this.product = product;
        }

        @Override
        public Product getProduct() {
            return product;
        }

        private void update(Timestamp stamp, double interest) {
            annualInterest.put(stamp.getYear(), interest);
        }

        @Override
        public double getInterest(int year) {
            return annualInterest.getOrDefault(year, 0.0);
        }
    }
}
