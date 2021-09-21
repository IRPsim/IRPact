package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;

import java.util.Map;

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
}
