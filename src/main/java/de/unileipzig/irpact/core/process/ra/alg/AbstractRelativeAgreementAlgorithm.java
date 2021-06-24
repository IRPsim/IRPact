package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRelativeAgreementAlgorithm extends SimulationEntityBase implements RelativeAgreementAlgorithm {

    protected boolean logDataFallback;

    public AbstractRelativeAgreementAlgorithm() {
    }

    public void setLogDataFallback(boolean logDataFallback) {
        this.logDataFallback = logDataFallback;
    }

    public boolean isLogDataFallback() {
        return logDataFallback;
    }

    public boolean isLogData() {
        return environment == null
                ? logDataFallback
                : environment.getSettings().isLogRelativeAgreement();
    }
}
