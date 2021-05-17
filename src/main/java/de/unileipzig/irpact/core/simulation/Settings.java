package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.start.MainCommandLineOptions;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public interface Settings {

    //=========================
    //general
    //=========================

    void apply(MainCommandLineOptions clOptions);

    boolean prefereCsv();

    //=========================
    //run and previous run
    //=========================

    boolean hasPreviousRun();

    boolean isFirstRun();

    void setNumberOfPreviousRuns(int runs);

    int getCurrentRun();

    int getNumberOfPreviousRuns();

    void setLastSimulationYearOfPreviousRun(int year);

    int getLastSimulationYearOfPreviousRun();

    void setContinueFromPreviousRun(boolean continueSimulation);

    boolean isContinueFromPreviousRun();

    //=========================
    //time
    //=========================

    void setFirstSimulationYear(int year);

    int getFirstSimulationYear();

    void setLastSimulationYear(int year);

    int getLastSimulationYear();

    int getNumberOfSimulationYears();

    IntStream streamSimulationYears();

    int[] getSimulationYears();

    List<Number> listYears();

    boolean hasMultipleSimulationYears();

    //=========================
    //actuel years
    //=========================

    int getActualFirstSimulationYear();

    int getActualNumberOfSimulationYears();

    boolean hasActualMultipleSimulationYears();

    IntStream streamActualSimulationYears();

    int[] getActualSimulationYears();

    List<Number> listActualYears();

    //=========================
    //population size
    //=========================

    boolean hasInitialNumberOfConsumerAgents(ConsumerAgentGroup group);

    void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int size);

    int getInitialNumberOfConsumerAgents(ConsumerAgentGroup group);

    //=========================
    //data logging
    //=========================

    void setLogGraphUpdate(boolean log);
    boolean isLogGraphUpdate();

    void setLogInterestUpdate(boolean log);
    boolean isLogInterestUpdate();

    void setLogRelativeAgreement(boolean log);
    boolean isLogRelativeAgreement();

    void setLogShareNetworkLocale(boolean log);
    boolean isLogShareNetworkLocale();

    void setLogFinancialComponent(boolean log);
    boolean isLogFinancialComponent();

    void setLogCalculateDecisionMaking(boolean log);
    boolean isLogCalculateDecisionMaking();

    //=========================
    //result logging
    //=========================

    void setLogResultGroupedByZipAndMilieu(boolean log);
    boolean isLogResultGroupedByZipAndMilieu();

    void setLogResultGroupedByZip(boolean log);
    boolean isLogResultGroupedByZip();

    void setLogResultGroupedByMilieu(boolean log);
    boolean isLogResultGroupedByMilieu();

    void setLogProductAdoptions(boolean log);
    boolean isLogProductAdoptions();
}
