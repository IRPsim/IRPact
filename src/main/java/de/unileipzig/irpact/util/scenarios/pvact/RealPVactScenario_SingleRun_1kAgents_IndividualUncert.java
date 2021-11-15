package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InTruncatedNormalDistribution;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InCommunicationModule_actiongraphnode2;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.util.pvact.Milieu;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class RealPVactScenario_SingleRun_1kAgents_IndividualUncert extends RealPVactScenario_SingleRun_1kAgents {

    public static final int REVISION = 0;

    public RealPVactScenario_SingleRun_1kAgents_IndividualUncert(String name, String creator, String description) {
        super(name, creator, description);
        setRevision(REVISION);
    }

    @Override
    protected void setupCommunicationModuleLogging(InCommunicationModule_actiongraphnode2 module) {
        module.setRaOpinionLogging(true);
        module.setRaUnceraintyLogging(true);
    }

    @Override
    protected InUncertaintySupplier createUncertainty(String name) {
        return createInPVactIndividualGlobalModerateExtremistUncertaintySupplier(
                name,
                RAConstants.DEFAULT_EXTREMIST_RATE,
                RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY,
                RAConstants.DEFAULT_MODERATE_UNCERTAINTY
        );
    }
}
