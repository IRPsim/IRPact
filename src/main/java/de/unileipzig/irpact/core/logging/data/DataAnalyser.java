package de.unileipzig.irpact.core.logging.data;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

/**
 * @author Daniel Abitz
 */
public interface DataAnalyser {

    //=========================
    //phase transition
    //=========================

    void setLogPhaseTransition(boolean enable);

    boolean isLogPhaseTransition();

    void logPhaseTransition(ConsumerAgent agent, Phase phase, Product product, Timestamp stamp);

    Map<Phase, Integer> getTransitionOverviewForYear(Product product, int year);

    Phase getPhaseFor(ConsumerAgent agent, Product product, int year);

    /**
     * @author Daniel Abitz
     */
    enum Phase {
        UNKNOWN,
        INITIAL_ADOPTED,
        AWARENESS,
        FEASIBILITY,
        DECISION_MAKING,
        ADOPTED
    }

    /**
     * @author Daniel Abitz
     */
    interface PhaseTransition extends Comparable<PhaseTransition> {

        Timestamp getStamp();

        Product getProduct();

        Phase getPhase();
    }

    //=========================
    //annual interest
    //=========================

    void setLogAnnualInterest(boolean value);

    boolean isLogAnnualInterest();

    void logAnnualInterest(ConsumerAgent agent, Product product, double interest, Timestamp stamp);

    double getAnnualInterest(ConsumerAgent agent, Product product, int year);

    int getCumulatedAnnualInterestCount(Product product, int year, double interest);

    //=========================
    //annual evaluation data
    //=========================

    void setLogEvaluationData(boolean value);

    boolean isLogEvaluationData();

    void setEvaluationBucketSize(double size);

    double getEvaluationBucketSize();

    DecimalFormat getEvaluationBucketFormatter();

    void logEvaluationData(
            Product product, Timestamp stamp,
            boolean valid,
            double a, double b, double c, double d,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double adoptionFactor
    );

    Bucket getNaNBucket();

    List<Bucket> createAllBuckets(int from, int to);

    NavigableSet<Bucket> getBuckets();

    EvaluationData getEvaluationData(boolean valid, Product product, int year, Bucket bucket);

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
    //annual agent state
    //=========================

    AgentDataState getAgentDataState(ConsumerAgent agent, Product product, int year);

    //ASD gucke impl
    void logAgentDataState(ConsumerAgent agent, Product product, int year, AgentDataState state);
}
