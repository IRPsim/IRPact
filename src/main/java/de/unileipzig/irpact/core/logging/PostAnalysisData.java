package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

/**
 * @author Daniel Abitz
 */
public interface PostAnalysisData {

    //=========================
    //general
    //=========================

    int getNumberOfInitialAdopter(Product product);

    //=========================
    //phase transition
    //=========================

    int UNKNOWN = -1;
    int INITIAL_ADOPTED = 0;
    int AWARENESS = 1;
    int FEASIBILITY = 2;
    int DECISION_MAKING = 3;
    int ADOPTED = 4;

    void setLogPhaseTransition(boolean enable);

    boolean isLogPhaseTransition();

    void logPhaseTransition(ConsumerAgent agent, int phase, Product product, Timestamp stamp);

    Map<Integer, Integer> getTransitionOverviewForYear(Product product, int year);

    int getPhaseFor(ConsumerAgent agent, Product product, int year);

    /**
     * @author Daniel Abitz
     */
    interface PhaseTransition extends Comparable<PhaseTransition> {

        Timestamp getStamp();

        Product getProduct();

        int getPhase();
    }

    //=========================
    //annual interest
    //=========================

    void setLogAnnualInterest(boolean value);

    boolean isLogAnnualInterest();

    void logAnnualInterest(ConsumerAgent agent, Product product, double interest, Timestamp stamp);

    CumulatedAnnualInterest getCumulatedAnnualInterest(Product product, int year);

    int getCumulatedAnnualInterestCount(Product product, int year, double interest);

    double getAnnualInterest(ConsumerAgent agent, Product product, int year);

    /**
     * @author Daniel Abitz
     */
    interface CumulatedAnnualInterest {

        int getYear();

        Product getProduct();

        Map<Double, Integer> getInterest();

        default int getInterestCount(double value) {
            return getInterest().getOrDefault(value, 0);
        }
    }

    /**
     * @author Daniel Abitz
     */
    interface AnnualInterest {

        Product getProduct();

        double getInterest(int year);
    }

    //=========================
    //annual evaluation data
    //=========================

    void setLogEvaluationData(boolean value);

    boolean isLogEvaluationData();

    void setEvaluationBucketSize(double size);

    double getEvaluationBucketSize();

    DecimalFormat getEvaluationBucketFormatter();

    void logEvaluationData2(
            Product product, Timestamp stamp,
            double a, double b, double c, double d,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double adoptionFactor
    );

    Bucket getNaNBucket();

    List<Bucket> createAllBuckets(int from, int to);

    NavigableSet<Bucket> getBuckets();

    EvaluationData getEvaluationData(Product product, int year, Bucket bucket);

    /**
     * @author Daniel Abitz
     */
    interface Bucket extends Comparable<Bucket> {

        boolean isNaN();

        double getFrom();

        double getTo();

        String print();

        String print(DecimalFormat format);
    }

    /**
     * @author Daniel Abitz
     */
    interface EvaluationData {

        int countA();

        int countB();

        int countC();

        int countD();

        int countAA();

        int countBB();

        int countCC();

        int countDD();

        int countWeightedAA();

        int countWeightedBB();

        int countWeightedCC();

        int countWeightedDD();

        int countAdoptionFactor();
    }

    //=========================
    //full evaluation
    //=========================

    void setLogAllEvaluationData(boolean value);

    boolean isLogAllEvaluationData();

    void setLogAllEvaluationTemp(Path target);

    void finishAllEvaluation(boolean cleanup) throws IOException;

    void logAllEvaluationDataFinancialFailed(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double financialThreshold, double financialValue
    );

    void logAllEvaluationData(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double aWeight, double bWeight, double cWeight, double dWeight,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double financialThreshold, double financialValue,
            double adoptionThreshold, double adoptionValue
    );
}
