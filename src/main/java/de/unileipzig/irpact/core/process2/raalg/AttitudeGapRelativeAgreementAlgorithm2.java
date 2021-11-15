package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.EnumMap;
import java.util.Map;

import static de.unileipzig.irpact.core.process2.raalg.RelativeAgreementAlgorithm2Util.*;

/**
 * @author Daniel Abitz
 */
public class AttitudeGapRelativeAgreementAlgorithm2 extends BasicRelativeAgreementAlgorithm2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AttitudeGapRelativeAgreementAlgorithm2.class);

    protected EnumMap<AttitudeGap, Double> weights = new EnumMap<>(AttitudeGap.class);
    protected WeightedMapping<AttitudeGap> mapping;
    protected double attitudeGap;
    protected Rnd rnd;

    public AttitudeGapRelativeAgreementAlgorithm2() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }

    public double getAttitudeGap() {
        return attitudeGap;
    }

    public synchronized void setWeight(AttitudeGap mode, double weight) {
        if(weight > 0) {
            weights.put(mode, weight);
        } else {
            weights.remove(mode);
        }
        mapping = null;
    }

    protected WeightedMapping<AttitudeGap> getMapping() {
        if(mapping == null) {
            return syncGetMapping();
        } else {
            return mapping;
        }
    }

    protected synchronized WeightedMapping<AttitudeGap> syncGetMapping() {
        if(mapping == null) {
            NavigableMapWeightedMapping<AttitudeGap> mapping = new NavigableMapWeightedMapping<>();
            for(Map.Entry<AttitudeGap, Double> entry: weights.entrySet()) {
                trace("[{}] put mode={}, weight={}", getName(), entry.getKey(), entry.getValue());
                mapping.set(entry.getKey(), entry.getValue());
            }
            this.mapping = mapping;
        }
        return mapping;
    }

    @Override
    public boolean calculateInfluence(double xi, double ui, double xj, double uj, double[] influence) {
        validate(influence);
        double gab = gab(xi, xj);
        if(gab < getAttitudeGap()) {
            return calculateWithinGab(xi, ui, xj, uj, influence);
        } else {
            return calculateWithMode(xi, ui, xj, uj, influence);
        }
    }

    protected boolean calculateWithinGab(double xi, double ui, double xj, double uj, double[] influence) {
        return calculateConvergence(xi, ui, xj, uj, influence);
    }

    protected boolean calculateWithMode(double xi, double ui, double xj, double uj, double[] influence) {
        WeightedMapping<AttitudeGap> mapping = getMapping();

        if(mapping.isEmpty()) {
            throw new IllegalStateException("empty weight mapping");
        }

        AttitudeGap mode = mapping.getWeightedRandom(getRnd());
        switch (mode) {
            case NEUTRAL:
                return calculateNeutral(xi, ui, xj, uj, influence);

            case CONVERGENCE:
                return calculateConvergence(xi, ui, xj, uj, influence);

            case DIVERGENCE:
                return calculateDivergence(xi, ui, xj, uj, influence);

            default:
                throw new IllegalStateException("unsupported attide gab mode: " + mode);
        }
    }

    protected boolean handleDisabled(double xi, double ui, double xj, double uj, double[] influence) {
        influence[RelativeAgreementAlgorithm2.INDEX_XI] = xi;
        influence[RelativeAgreementAlgorithm2.INDEX_UI] = ui;
        influence[RelativeAgreementAlgorithm2.INDEX_XJ] = xj;
        influence[RelativeAgreementAlgorithm2.INDEX_UJ] = uj;
        return false;
    }

    protected boolean calculateNeutral(double xi, double ui, double xj, double uj, double[] influence) {
        return handleDisabled(xi, ui, xj, uj, influence);
    }

    protected boolean calculateConvergence(double xi, double ui, double xj, double uj, double[] influence) {
        return mutualRa(true, xi, ui, xj, uj, getSpeedOfConvergence(), influence, 0);
    }

    protected boolean calculateDivergence(double xi, double ui, double xj, double uj, double[] influence) {
        return mutualRa(false, xi, ui, xj, uj, getSpeedOfConvergence(), influence, 0);
    }
}
