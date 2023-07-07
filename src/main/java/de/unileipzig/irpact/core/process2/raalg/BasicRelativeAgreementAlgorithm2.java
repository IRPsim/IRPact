package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.core.process2.raalg.RelativeAgreementAlgorithm2Util.*;

/**
 * @author Daniel Abitz
 */
public class BasicRelativeAgreementAlgorithm2
        extends NameableBase
        implements RelativeAgreementAlgorithm2, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicRelativeAgreementAlgorithm2.class);

    protected double speedOfConvergence;

    public BasicRelativeAgreementAlgorithm2() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    @Override
    public boolean calculateInfluence(double xi, double ui, double xj, double uj, double[] influence) {
        validate(influence);
        return mutualRa(true, xi, ui, xj, uj, getSpeedOfConvergence(), influence, 0);
    }
}
