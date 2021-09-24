package de.unileipzig.irpact.core.simulation;

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

    List<Integer> listYears();

    boolean hasMultipleSimulationYears();

    //=========================
    //actuel years
    //=========================

    int getActualFirstSimulationYear();

    int getActualNumberOfSimulationYears();

    boolean hasActualMultipleSimulationYears();

    IntStream streamActualSimulationYears();

    int[] getActualSimulationYears();

    List<Integer> listActualYears();

    //=========================
    //result logging
    //=========================

    void setLogResultAdoptionsAll(boolean log);
    boolean isLogResultAdoptionsAll();

    void setLogPerformance(boolean log);
    boolean isLogPerformance();

    //=========================
    //script logging
    //=========================

    void setLogScriptAdoptionsZip(boolean log);
    boolean isLogScriptAdoptionsZip();

    void setLogScriptAdoptionsZipPhase(boolean log);
    boolean isLogScriptAdoptionsZipPhase();
}
