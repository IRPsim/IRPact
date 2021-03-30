package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.start.CommandLineOptions;

/**
 * @author Daniel Abitz
 */
public interface Settings {

    //=========================
    //general
    //=========================

    void apply(CommandLineOptions clOptions);

    boolean ignorePersistenceCheckResult();

    //=========================
    //extra persist data
    //=========================

    boolean hasPreviousRun();

    boolean isFirstRun();

    void setNumberOfPreviousRuns(int runs);

    int getCurrentRun();

    void setLastSimulationYearOfPreviousRun(int year);

    int getLastSimulationYearOfPreviousRun();

    int getActualFirstSimulationYear();

    int getActualNumberOfSimulationYears();

    boolean hasActualMultipleSimulationYears();

    //=========================
    //time
    //=========================

    void setFirstSimulationYear(int year);

    int getFirstSimulationYear();

    void setLastSimulationYear(int year);

    int getLastSimulationYear();

    int getNumberOfSimulationYears();

    boolean hasMultipleSimulationYears();

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
