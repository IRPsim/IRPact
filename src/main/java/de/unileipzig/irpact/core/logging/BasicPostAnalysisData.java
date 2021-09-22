package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    @Override
    public int getPhaseFor(ConsumerAgent agent, Product product, int year) {
        if(logPhaseTransition) {
            PhaseTransitionList list = transitionMap.get(agent);
            if(list == null) return UNKNOWN;
            BasicPhaseTransition transition = list.getLatestInfoUntilYear(product, year);
            return transition == null
                    ? UNKNOWN
                    : transition.getPhase();
        } else {
            throw new IllegalStateException();
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

    //=========================
    //evaluation data
    //=========================

    protected static final BasicBucket NAN_BUCKET = new BasicBucket(Double.NaN, Double.NaN);
    protected final Map<Double, BasicBucket> bucketCache = new ConcurrentHashMap<>();
    protected final Map<Product, Map<Integer, Map<Bucket, BasicEvaluationData>>> productAnnualBucketMap = new ConcurrentHashMap<>();
    protected boolean logEvaluationData = true;
    protected double bucketSize = 0.1;
    protected DecimalFormat bucketPrinter = buildFormat(2);

    protected static DecimalFormat buildFormat(int digits) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(digits);
        format.setDecimalFormatSymbols(symbols);
        return format;
    }

    protected BasicBucket getBucket0(double from) {
        if(bucketCache.containsKey(from)) {
            return bucketCache.get(from);
        } else {
            double to = from + bucketSize;
            BasicBucket bucket = new BasicBucket(from, to);
            bucketCache.put(from, bucket);
            return bucket;
        }
    }

    protected BasicBucket getBucket(double value) {
        if(Double.isNaN(value)) {
            return NAN_BUCKET;
        } else {
            double from = Math.floor(value / bucketSize) * bucketSize;
            return getBucket0(from);
        }
    }

    protected BasicEvaluationData getData(Product product, Timestamp stamp, BasicBucket bucket) {
        Map<Integer, Map<Bucket, BasicEvaluationData>> annualBucketMap = productAnnualBucketMap.computeIfAbsent(product, _product -> new ConcurrentHashMap<>());
        Map<Bucket, BasicEvaluationData> bucketMap = annualBucketMap.computeIfAbsent(stamp.getYear(), _year -> new ConcurrentHashMap<>());
        return bucketMap.computeIfAbsent(bucket, _bucket -> new BasicEvaluationData());
    }

    @Override
    public void setLogEvaluationData(boolean logEvaluationData) {
        this.logEvaluationData = logEvaluationData;
    }

    @Override
    public boolean isLogEvaluationData() {
        return logEvaluationData;
    }

    @Override
    public void setEvaluationBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
        String str = Double.toString(bucketSize);
        int decimalIndex = str.indexOf('.');
        int digits = decimalIndex == -1
                ? 0
                : str.length() - decimalIndex - 1;
        bucketPrinter = buildFormat(digits);
    }

    @Override
    public double getEvaluationBucketSize() {
        return bucketSize;
    }

    @Override
    public DecimalFormat getEvaluationBucketFormatter() {
        return bucketPrinter;
    }

    @Override
    public void logEvaluationData2(
            Product product, Timestamp stamp,
            double a, double b, double c, double d,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double adoptionFactor) {
        getData(product, stamp, getBucket(a)).updateA();
        getData(product, stamp, getBucket(b)).updateB();
        getData(product, stamp, getBucket(c)).updateC();
        getData(product, stamp, getBucket(d)).updateD();
        getData(product, stamp, getBucket(aa)).updateAA();
        getData(product, stamp, getBucket(bb)).updateBB();
        getData(product, stamp, getBucket(cc)).updateCC();
        getData(product, stamp, getBucket(dd)).updateDD();
        getData(product, stamp, getBucket(weightedAA)).updateWeightedAA();
        getData(product, stamp, getBucket(weightedBB)).updateWeightedBB();
        getData(product, stamp, getBucket(weightedCC)).updateWeightedCC();
        getData(product, stamp, getBucket(weightedDD)).updateWeightedDD();
        getData(product, stamp, getBucket(adoptionFactor)).updateAdoptionFactor();
    }

    @Override
    public Bucket getNaNBucket() {
        return NAN_BUCKET;
    }

    @Override
    public List<Bucket> createAllBuckets(int from, int to) {
        List<Bucket> list = new ArrayList<>();
        for(double f = from; f < to; f += bucketSize) {
            BasicBucket bucket = getBucket0(f);
            list.add(bucket);
        }
        return list;
    }

    @Override
    public NavigableSet<Bucket> getBuckets() {
        NavigableSet<Bucket> buckets = new TreeSet<>();
        productAnnualBucketMap.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .map(Map::keySet)
                .forEach(buckets::addAll);
        return buckets;
    }

    @Override
    public EvaluationData getEvaluationData(Product product, int year, Bucket bucket) {
        Map<Integer, Map<Bucket, BasicEvaluationData>> annualData = productAnnualBucketMap.get(product);
        if(annualData == null) return null;
        Map<Bucket, BasicEvaluationData> bucketData = annualData.get(year);
        return bucketData == null
                ? null
                : bucketData.get(bucket);
    }

    /**
     * @author Daniel Abitz
     */
    private static final class BasicBucket implements Bucket {

        private final double from;
        private final double to;

        private BasicBucket(double from, double to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BasicBucket)) return false;
            BasicBucket bucket = (BasicBucket) o;
            return Double.compare(bucket.from, from) == 0 && Double.compare(bucket.to, to) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public boolean isNaN() {
            return Double.isNaN(from);
        }

        @Override
        public double getFrom() {
            return from;
        }

        @Override
        public double getTo() {
            return to;
        }

        @Override
        public String print() {
            return isNaN()
                    ? "NaN"
                    : "[" + getFrom() + ", " + getTo() + ")";
        }

        @Override
        public String print(DecimalFormat format) {
            if(format == null || isNaN()) {
                return print();
            } else {
                return "[" + format.format(getFrom()) + ", " + format.format(getTo()) + ")";
            }
        }

        @Override
        public int compareTo(Bucket o) {
            if(isNaN()) {
                if(o.isNaN()) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if(o.isNaN()) {
                    return 1;
                } else {
                    return Double.compare(getFrom(), o.getFrom());
                }
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static final class BasicEvaluationData implements EvaluationData {

        private int a;
        private int b;
        private int c;
        private int d;
        private int aa;
        private int bb;
        private int cc;
        private int dd;
        private int weightedAA;
        private int weightedBB;
        private int weightedCC;
        private int weightedDD;
        private int adoptionFactor;

        private BasicEvaluationData() {
        }

        private void updateA() {
            a++;
        }

        private void updateB() {
            b++;
        }

        private void updateC() {
            c++;
        }

        private void updateD() {
            d++;
        }

        private void updateAA() {
            aa++;
        }

        private void updateBB() {
            bb++;
        }

        private void updateCC() {
            cc++;
        }

        private void updateDD() {
            dd++;
        }

        private void updateWeightedAA() {
            weightedAA++;
        }

        private void updateWeightedBB() {
            weightedBB++;
        }

        private void updateWeightedCC() {
            weightedCC++;
        }

        private void updateWeightedDD() {
            weightedDD++;
        }

        private void updateAdoptionFactor() {
            adoptionFactor++;
        }

        @Override
        public int countA() {
            return a;
        }

        @Override
        public int countB() {
            return b;
        }

        @Override
        public int countC() {
            return c;
        }

        @Override
        public int countD() {
            return d;
        }

        @Override
        public int countAA() {
            return aa;
        }

        @Override
        public int countBB() {
            return bb;
        }

        @Override
        public int countCC() {
            return cc;
        }

        @Override
        public int countDD() {
            return dd;
        }

        @Override
        public int countWeightedAA() {
            return weightedAA;
        }

        @Override
        public int countWeightedBB() {
            return weightedBB;
        }

        @Override
        public int countWeightedCC() {
            return weightedCC;
        }

        @Override
        public int countWeightedDD() {
            return weightedDD;
        }

        @Override
        public int countAdoptionFactor() {
            return adoptionFactor;
        }
    }

    //=========================
    //full evaluation
    //=========================

    public static final String NO_VALUE = "-";
    private static final int FORMAT_LEN = 28;
    private static final String LOGGER_NAME = "ALL_EVAL_LOGGER";
    private static final String APPENDER_NAME = "ALL_EVAL_APPENDER";
    private static final String FORMAT = BasicPostAnalysisLogger.createFormat(FORMAT_LEN);
    private boolean logAllEvaluationData = false;
    private SimplifiedFileLogger evalLogger;

    @Override
    public void setLogAllEvaluationData(boolean value) {
        logAllEvaluationData = true;
    }

    @Override
    public boolean isLogAllEvaluationData() {
        return logAllEvaluationData;
    }

    @Override
    public void setLogAllEvaluationTemp(Path target) {
        if(logAllEvaluationData) {
            try {
                LOGGER.trace("create logger: {}", target);
                evalLogger = new SimplifiedFileLogger(
                        LOGGER_NAME,
                        SimplifiedFileLogger.createNew(
                                APPENDER_NAME,
                                BasicPostAnalysisLogger.DEFAULT_PATTERN,
                                target
                        )
                );
            } catch (IOException e) {
                LOGGER.error("logger creation failed, disable log all evaluations", e);
                logAllEvaluationData = false;
            }
        } else {
            LOGGER.trace("log all evalution is disabled");
            evalLogger = null;
        }
    }

    @Override
    public void finishAllEvaluation(boolean cleanup) throws IOException {
        if(evalLogger != null) {
            evalLogger.stop();
            if(cleanup) {
                Path target = evalLogger.getTarget();
                Files.deleteIfExists(target);
            }
        }
    }

    @Override
    public void logAllEvaluationDataFinancialFailed(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double financialThreshold, double financialValue) {
        if(evalLogger != null) {
            evalLogger.log(FORMAT,
                    agent.getName(), product.getName(), BasicPostAnalysisLogger.printStamp(stamp), stamp.getYear(),
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    financialThreshold, financialValue,
                    NO_VALUE, NO_VALUE
            );
        }
    }

    @Override
    public void logAllEvaluationData(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double aWeight, double bWeight, double cWeight, double dWeight,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double financialThreshold, double financialValue,
            double adoptionThreshold, double adoptionValue) {
        if(evalLogger != null) {
            evalLogger.log(FORMAT,
                    agent.getName(), product.getName(), BasicPostAnalysisLogger.printStamp(stamp), stamp.getYear(),
                    aWeight, bWeight, cWeight, dWeight,
                    a, b, c, d,
                    aValue, bValue, cValue, dValue,
                    aa, bb, cc, dd,
                    weightedAA, weightedBB, weightedCC, weightedDD,
                    financialThreshold, financialValue,
                    adoptionThreshold, adoptionValue
            );
        }
    }
}
