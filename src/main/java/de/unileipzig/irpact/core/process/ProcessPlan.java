package de.unileipzig.irpact.core.process;

/**
 * @author Daniel Abitz
 */
public interface ProcessPlan {

    ProcessPlanResult execute();

    void onAdopted();
}
