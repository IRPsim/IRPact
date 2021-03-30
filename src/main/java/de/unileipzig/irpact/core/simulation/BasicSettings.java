package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.start.CommandLineOptions;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicSettings implements Settings {

    protected Map<ConsumerAgentGroup, Integer> agentCount;
    protected int firstSimulationYear;
    protected int lastSimulationYear = -1;
    protected boolean ignorePersistCheck = false;
    protected int run = 1;
    protected int previousLastSimulationYear = -1;
    protected boolean prefereCsv = false;

    protected boolean logRelativeAgreement = false;
    protected boolean logInterestUpdate = false;
    protected boolean logGraphUpdate = false;
    protected boolean logShareNetworkLocale = false;
    protected boolean logResultGroupedByZipAndMilieu = false;
    protected boolean logResultGroupedByZip = false;
    protected boolean logResultGroupedByMilieu = false;
    protected boolean logProductAdoptions = false;

    public BasicSettings() {
        this(new LinkedHashMap<>());
    }

    public BasicSettings(
            Map<ConsumerAgentGroup, Integer> agentCount) {
        this.agentCount = agentCount;
    }

    //=========================
    //general
    //=========================

    @Override
    public void apply(CommandLineOptions clOptions) {
        setIgnorePersistenceCheckResult(clOptions.isIgnorePersistenceCheck());
        setPrefereCsv(clOptions.isPrefereCsv());
    }

    public void setIgnorePersistenceCheckResult(boolean value) {
        ignorePersistCheck = value;
    }

    @Override
    public boolean ignorePersistenceCheckResult() {
        return ignorePersistCheck;
    }

    @Override
    public boolean prefereCsv() {
        return prefereCsv;
    }

    public void setPrefereCsv(boolean prefereCsv) {
        this.prefereCsv = prefereCsv;
    }

    //=========================
    //extra persist data
    //=========================

    @Override
    public boolean hasPreviousRun() {
        return run != 1;
    }

    @Override
    public boolean isFirstRun() {
        return run == 1;
    }

    @Override
    public void setNumberOfPreviousRuns(int runs) {
        this.run = Math.max(runs, 0) + 1;
    }

    @Override
    public int getCurrentRun() {
        return run;
    }

    @Override
    public void setLastSimulationYearOfPreviousRun(int year) {
        this.previousLastSimulationYear = year;
    }

    @Override
    public int getLastSimulationYearOfPreviousRun() {
        return previousLastSimulationYear;
    }

    @Override
    public int getActualFirstSimulationYear() {
        return hasPreviousRun()
                ? getLastSimulationYearOfPreviousRun() + 1
                : getFirstSimulationYear();
    }

    @Override
    public int getActualNumberOfSimulationYears() {
        return Math.max(lastSimulationYear - getActualFirstSimulationYear(), 0) + 1;
    }

    @Override
    public boolean hasActualMultipleSimulationYears() {
        return getActualNumberOfSimulationYears() > 1;
    }

    //=========================
    //time
    //=========================

    @Override
    public void setFirstSimulationYear(int year) {
        this.firstSimulationYear = year;
    }

    @Override
    public int getFirstSimulationYear() {
        return firstSimulationYear;
    }

    @Override
    public void setLastSimulationYear(int year) {
        this.lastSimulationYear = year;
    }

    @Override
    public int getLastSimulationYear() {
        return hasMultipleSimulationYears()
                ? lastSimulationYear
                : firstSimulationYear;
    }

    @Override
    public int getNumberOfSimulationYears() {
        return Math.max(lastSimulationYear - firstSimulationYear, 0) + 1;
    }

    @Override
    public boolean hasMultipleSimulationYears() {
        return getNumberOfSimulationYears() > 1;
    }

    //=========================
    //population size
    //=========================

    @Override
    public boolean hasInitialNumberOfConsumerAgents(ConsumerAgentGroup group) {
        return agentCount.containsKey(group);
    }

    @Override
    public void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int size) {
        agentCount.put(group, size);
    }

    @Override
    public int getInitialNumberOfConsumerAgents(ConsumerAgentGroup group) {
        Integer count = agentCount.get(group);
        if(count == null) {
            throw new NoSuchElementException(group.getName());
        }
        return count;
    }

    //=========================
    //data logging
    //=========================

    @Override
    public void setLogGraphUpdate(boolean log) {
        this.logGraphUpdate = log;
    }
    @Override
    public boolean isLogGraphUpdate() {
        return logGraphUpdate;
    }

    @Override
    public void setLogInterestUpdate(boolean log) {
        this.logInterestUpdate = log;
    }
    @Override
    public boolean isLogInterestUpdate() {
        return logInterestUpdate;
    }

    @Override
    public void setLogRelativeAgreement(boolean log) {
        this.logRelativeAgreement = log;
    }
    @Override
    public boolean isLogRelativeAgreement() {
        return logRelativeAgreement;
    }

    @Override
    public void setLogShareNetworkLocale(boolean log) {
        this.logShareNetworkLocale = log;
    }
    @Override
    public boolean isLogShareNetworkLocale() {
        return logShareNetworkLocale;
    }

    //=========================
    //result logging
    //=========================

    @Override
    public void setLogResultGroupedByZipAndMilieu(boolean log) {
        this.logResultGroupedByZipAndMilieu = log;
    }
    @Override
    public boolean isLogResultGroupedByZipAndMilieu() {
        return logResultGroupedByZipAndMilieu;
    }

    @Override
    public void setLogResultGroupedByZip(boolean log) {
        this.logResultGroupedByZip = log;
    }
    @Override
    public boolean isLogResultGroupedByZip() {
        return logResultGroupedByZip;
    }

    @Override
    public void setLogResultGroupedByMilieu(boolean log) {
        this.logResultGroupedByMilieu = log;
    }
    @Override
    public boolean isLogResultGroupedByMilieu() {
        return logResultGroupedByMilieu;
    }

    @Override
    public void setLogProductAdoptions(boolean log) {
        this.logProductAdoptions = log;
    }
    @Override
    public boolean isLogProductAdoptions() {
        return logProductAdoptions;
    }
}
