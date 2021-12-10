package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_3 extends ToyModel_R_2 {

    public static final int REVISION = 0;

    public ToyModel_R_3(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected InUncertaintySupplier createUncertainty(String name) {
        return createInPVactUpdatableGlobalModerateExtremistUncertainty(
                "uncert",
                0.1,
                0.05,
                0.2
        );
    }

    @Override
    protected void setupTemplate(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setRaEnabled(true);
    }

    @Override
    protected void setupCagForR(InPVactConsumerAgentGroup cag) {
        cag.setB6(dirac0);
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        mpm.setAllWeights(0);
        mpm.getNpvWeightModule().setScalar(0.25);
        mpm.getPpWeightModule().setScalar(0.25);
        mpm.getEnvWeightModule().setScalar(0.25);
        mpm.getNovWeightModule().setScalar(0.25);
    }
}
