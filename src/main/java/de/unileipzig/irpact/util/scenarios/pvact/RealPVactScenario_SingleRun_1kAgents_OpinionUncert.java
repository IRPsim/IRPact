package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InCommunicationModule3;

/**
 * @author Daniel Abitz
 */
public class RealPVactScenario_SingleRun_1kAgents_OpinionUncert extends RealPVactScenario_SingleRun_1kAgents {

    public static final int REVISION = 0;

    public RealPVactScenario_SingleRun_1kAgents_OpinionUncert(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    protected void setupCommunicationModuleLogging(InCommunicationModule3 module) {
        module.setRaOpinionLogging(true);
        module.setRaUnceraintyLogging(false);
    }

    protected InUncertaintySupplier createUncertainty(String name) {
        return createInPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion(
                name,
                RAConstants.DEFAULT_EXTREMIST_RATE,
                RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY,
                RAConstants.DEFAULT_MODERATE_UNCERTAINTY
        );
    }
}
