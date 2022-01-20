package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.commons.distribution.RoundingMode;
import de.unileipzig.irpact.io.param.input.distribution.InTruncatedNormalDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.scenarios.pvact.util.RealDataModularProcessModelTemplate;

/**
 * @author Daniel Abitz
 */
public class RealPVactScenario_SingleRun_1kAgents_SpecialInterest extends RealPVactScenario_SingleRun_1kAgents {

    public static final int REVISION = 0;

    public RealPVactScenario_SingleRun_1kAgents_SpecialInterest(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    protected InUnivariateDoubleDistribution createInterestDistribution() {
        InTruncatedNormalDistribution dist = new InTruncatedNormalDistribution();
        dist.setName("INTEREST_WITH_ROUNDING");
        dist.setLowerBound(0);
        dist.setUpperBound(9);
        dist.setMean(4.5);
        dist.setStandardDeviation(2.0);
        dist.setRoundingMode(RoundingMode.ROUND);
        return dist;
    }

    @Override
    protected RealDataModularProcessModelTemplate createTemplate() {
        return createRealDataTemplateWithInterest(
                "Process",
                createUncertainty("uncert"),
                createNodeFilterScheme(2),
                createInterestDistribution()
        );
    }
}
