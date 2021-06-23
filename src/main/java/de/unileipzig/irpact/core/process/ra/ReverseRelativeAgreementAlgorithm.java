package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.attribute.AttributeUtil;
import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public class ReverseRelativeAgreementAlgorithm extends NameableBase implements RelativeAgreementAlgorithm {

    public static final ReverseRelativeAgreementAlgorithm INSTANCE = new ReverseRelativeAgreementAlgorithm();

    public ReverseRelativeAgreementAlgorithm() {
    }

    @Override
    public boolean apply(
            double m,
            double oi, double ui,
            double oj, double uj,
            DoubleAttribute ojAttr, DoubleAttribute ujAttr) {
        double hij = Math.min(oi + ui, oj + uj) - Math.max(oi - ui, oj - uj);
        if(hij > ui) {
            double ra = hij / ui - 1.0;
            double newOj = oj - m * ra * (oi - oj);
            double newUj = uj - m * ra * (ui - uj);
            AttributeUtil.setDoubleValue(ojAttr, newOj);
            AttributeUtil.setDoubleValue(ujAttr, newUj);
            return true;
        } else {
            return false;
        }
    }
}
