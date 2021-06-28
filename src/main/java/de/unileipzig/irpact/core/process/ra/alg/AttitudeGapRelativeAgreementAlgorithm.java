package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public class AttitudeGapRelativeAgreementAlgorithm extends AbstractRelativeAgreementAlgorithm {

    /**
     * @author Daniel Abitz
     */
    public enum Mode {
        NEUTRAL,
        CONVERGENCE,
        DIVERGENCE
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AttitudeGapRelativeAgreementAlgorithm.class);

    protected WeightedMapping<Mode> mapping = new NavigableMapWeightedMapping<>();
    protected Rnd rnd;
    protected double attitudeGap;

    public AttitudeGapRelativeAgreementAlgorithm() {
        setWeightes(0.5, 0.25, 0.25);
    }

    public void setWeightes(double neutral, double convergence, double divergence) {
        mapping.clear();
        mapping.set(Mode.NEUTRAL, neutral);
        mapping.set(Mode.CONVERGENCE, convergence);
        mapping.set(Mode.DIVERGENCE, divergence);
    }

    public double getWeight(Mode mode) {
        return mapping.getWeight(mode);
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }

    public double getAttitudeGap() {
        return attitudeGap;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getAttitudeGap(),
                isLogDataFallback(),
                getWeight(Mode.NEUTRAL),
                getWeight(Mode.CONVERGENCE),
                getWeight(Mode.DIVERGENCE),
                getRandom()
        );
    }

    @Override
    public boolean apply(
            String name1,
            ConsumerAgentAttribute opinion1,
            Uncertainty uncertainty1,
            String name2,
            ConsumerAgentAttribute opinion2,
            Uncertainty uncertainty2) {
        double gab = Math.abs(opinion1.asValueAttribute().getDoubleValue() - opinion2.asValueAttribute().getDoubleValue());
        if(gab < getAttitudeGap()) {
            return apply(false, name1, opinion1, uncertainty1, name2, opinion2, uncertainty2);
        } else {
            Mode mode = mapping.getWeightedRandom(getRandom());
            switch (mode) {
                case NEUTRAL:
                    return false;

                case CONVERGENCE:
                    return apply(false, name1, opinion1, uncertainty1, name2, opinion2, uncertainty2);

                case DIVERGENCE:
                    return apply(true, name1, opinion1, uncertainty1, name2, opinion2, uncertainty2);

                default:
                    throw new IllegalStateException("unsupported mode: " + mode);
            }
        }
    }

    protected boolean apply(
            boolean reverse,
            String name1,
            ConsumerAgentAttribute opinion1,
            Uncertainty uncertainty1,
            String name2,
            ConsumerAgentAttribute opinion2,
            Uncertainty uncertainty2) {
        double m1 = uncertainty1.getSpeedOfConvergence(opinion1);
        double o1 = opinion1.asValueAttribute().getDoubleValue();
        double u1 = uncertainty1.getUncertainty(opinion1);
        double m2 = uncertainty2.getSpeedOfConvergence(opinion2);
        double o2 = opinion2.asValueAttribute().getDoubleValue();
        double u2 = uncertainty2.getUncertainty(opinion2);

        IRPLogger logger = getLogger(loggingDisabled(), isLogData(), LOGGER);
        IRPSection section = getSection(isLogData());
        Level level = getLevel(isLogData());

        //1 influences 2
        boolean influenced2 = BasicRelativeAgreementAlgorithm.apply(
                logger, section, level,
                currentYear(),
                m2,
                name1, o1, u1,
                name2, o2, u2,
                opinion2, uncertainty2,
                reverse
        );
        //2 influences 1
        boolean influenced1 = BasicRelativeAgreementAlgorithm.apply(
                logger, section, level,
                currentYear(),
                m1,
                name2, o2, u2,
                name1, o1, u1,
                opinion1, uncertainty1,
                reverse
        );

        return influenced2 || influenced1;
    }
}
