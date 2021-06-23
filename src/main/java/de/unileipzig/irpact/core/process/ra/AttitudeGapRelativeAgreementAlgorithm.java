package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.attribute.DoubleAttribute;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;

/**
 * @author Daniel Abitz
 */
public class AttitudeGapRelativeAgreementAlgorithm extends NameableBase implements RelativeAgreementAlgorithm {

    public enum Mode {
        NEUTRAL,
        CONVERGENCE,
        DIVERGENCE
    }

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
    public boolean apply(
            double m,
            double oi, double ui,
            double oj, double uj,
            DoubleAttribute ojAttr, DoubleAttribute ujAttr) {
        double gab = Math.abs(oi - oj);
        if(gab < getAttitudeGap()) {
            return DefaultRelativeAgreementAlgorithm.INSTANCE.apply(m, oi, ui, oj, uj, ojAttr, ujAttr);
        } else {
            Mode mode = mapping.getWeightedRandom(getRandom());
            switch (mode) {
                case NEUTRAL:
                    return false;

                case CONVERGENCE:
                    return DefaultRelativeAgreementAlgorithm.INSTANCE.apply(m, oi, ui, oj, uj, ojAttr, ujAttr);

                case DIVERGENCE:
                    return ReverseRelativeAgreementAlgorithm.INSTANCE.apply(m, oi, ui, oj, uj, ojAttr, ujAttr);

                default:
                    throw new IllegalStateException("unsupported mode: " + mode);
            }
        }
    }
}
