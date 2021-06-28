package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.checksum.ChecksumCalculator;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.develop.AddToParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class BasicSettings implements Settings, ChecksumComparable {

    protected int firstSimulationYear;
    protected int lastSimulationYear = -1;
    protected int run = 1;
    protected int previousLastSimulationYear = -1;
    protected boolean continueSimulation = false;

    @AddToParam("InGeneral")
    protected boolean prefereCsv = false;

    protected boolean logRelativeAgreement = false;
    protected boolean logInterestUpdate = false;
    protected boolean logGraphUpdate = false;
    protected boolean logShareNetworkLocale = false;
    protected boolean logFinancalComponent = false;
    protected boolean logCalculateAdoption = false;

    protected boolean logResultAdoptionsZip = false;
    protected boolean logResultAdoptionsZipPhase = false;
    protected boolean logResultAdoptionsAll = false;

    protected boolean logScriptAdoptionsZip = false;
    protected boolean logScriptAdoptionsZipPhase = false;

    public BasicSettings() {
    }

    //=========================
    //checksum
    //=========================

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return ChecksumCalculator.DEFAULT_NONNULL_CHECKSUM;
    }

    //=========================
    //general
    //=========================

    @Override
    public void apply(MainCommandLineOptions clOptions) {
        setPrefereCsv(clOptions.isPrefereCsv());
    }

    @Override
    public boolean prefereCsv() {
        return prefereCsv;
    }

    public void setPrefereCsv(boolean prefereCsv) {
        this.prefereCsv = prefereCsv;
    }

    //=========================
    //run and previous run
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
        this.run = Math.max(runs, 1);
    }

    @Override
    public int getCurrentRun() {
        return run;
    }

    @Override
    public int getNumberOfPreviousRuns() {
        return run - 1;
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
    public void setContinueFromPreviousRun(boolean continueSimulation) {
        this.continueSimulation = continueSimulation;
    }

    @Override
    public boolean isContinueFromPreviousRun() {
        return continueSimulation;
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
        return lastSimulationYear < firstSimulationYear
                ? firstSimulationYear
                : lastSimulationYear;
    }

    @Override
    public int getNumberOfSimulationYears() {
        return Math.max(getLastSimulationYear() - getFirstSimulationYear(), 0) + 1;
    }

    @Override
    public IntStream streamSimulationYears() {
        return IntStream.rangeClosed(firstSimulationYear, getLastSimulationYear());
    }

    @Override
    public int[] getSimulationYears() {
        return streamSimulationYears().toArray();
    }

    @Override
    public List<Integer> listYears() {
        return streamSimulationYears().boxed()
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasMultipleSimulationYears() {
        return getNumberOfSimulationYears() > 1;
    }

    //=========================
    //actuel years
    //=========================

    @Override
    public int getActualFirstSimulationYear() {
        return hasPreviousRun() && isContinueFromPreviousRun()
                ? getLastSimulationYearOfPreviousRun() + 1
                : getFirstSimulationYear();
    }

    @Override
    public int getActualNumberOfSimulationYears() {
        return Math.max(getLastSimulationYear() - getActualFirstSimulationYear(), 0) + 1;
    }

    @Override
    public boolean hasActualMultipleSimulationYears() {
        return getActualNumberOfSimulationYears() > 1;
    }

    @Override
    public IntStream streamActualSimulationYears() {
        return IntStream.rangeClosed(getActualFirstSimulationYear(), getLastSimulationYear());
    }

    @Override
    public int[] getActualSimulationYears() {
        return streamActualSimulationYears().toArray();
    }

    @Override
    public List<Integer> listActualYears() {
        return streamActualSimulationYears().boxed()
                .collect(Collectors.toList());
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

    @Override
    public void setLogFinancialComponent(boolean log) {
        this.logFinancalComponent = log;
    }
    @Override
    public boolean isLogFinancialComponent() {
        return logFinancalComponent;
    }

    @Override
    public void setLogCalculateDecisionMaking(boolean log) {
        this.logCalculateAdoption = log;
    }
    @Override
    public boolean isLogCalculateDecisionMaking() {
        return logCalculateAdoption;
    }

    //=========================
    //result logging
    //=========================

    @Override
    public void setLogResultAdoptionsZip(boolean log) {
        logResultAdoptionsZip = log;
    }

    @Override
    public boolean isLogResultAdoptionsZip() {
        return logResultAdoptionsZip;
    }

    @Override
    public void setLogResultAdoptionsZipPhase(boolean log) {
        logResultAdoptionsZipPhase = log;
    }

    @Override
    public boolean isLogResultAdoptionsZipPhase() {
        return logResultAdoptionsZipPhase;
    }

    @Override
    public void setLogResultAdoptionsAll(boolean log) {
        logResultAdoptionsAll = log;
    }

    @Override
    public boolean isLogResultAdoptionsAll() {
        return logResultAdoptionsAll;
    }

    //=========================
    //script + data logging
    //=========================

    @Override
    public void setLogScriptAdoptionsZip(boolean log) {
        logScriptAdoptionsZip = log;
    }

    @Override
    public boolean isLogScriptAdoptionsZip() {
        return logScriptAdoptionsZip;
    }

    @Override
    public void setLogScriptAdoptionsZipPhase(boolean log) {
        logScriptAdoptionsZipPhase = log;
    }

    @Override
    public boolean isLogScriptAdoptionsZipPhase() {
        return logScriptAdoptionsZipPhase;
    }
}
