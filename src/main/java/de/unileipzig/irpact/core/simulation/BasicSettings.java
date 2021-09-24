package de.unileipzig.irpact.core.simulation;

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

    protected boolean logResultAdoptionsAll = false;
    protected boolean logPerformance = false;

    protected boolean logScriptAdoptionsZip = false;
    protected boolean logScriptAdoptionsZipPhase = false;

    public BasicSettings() {
    }

    //=========================
    //checksum
    //=========================

    @Override
    public int getChecksum() {
        return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
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
        return Math.max(lastSimulationYear, firstSimulationYear);
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
    //result logging
    //=========================

    @Override
    public void setLogResultAdoptionsAll(boolean log) {
        logResultAdoptionsAll = log;
    }

    @Override
    public boolean isLogResultAdoptionsAll() {
        return logResultAdoptionsAll;
    }

    @Override
    public void setLogPerformance(boolean log) {
        logPerformance = log;
    }

    @Override
    public boolean isLogPerformance() {
        return logPerformance;
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
