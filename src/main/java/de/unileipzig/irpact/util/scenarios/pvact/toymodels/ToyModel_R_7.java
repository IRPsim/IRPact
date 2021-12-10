package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_7 extends ToyModel_R_6 {

    public static final int REVISION = 0;

    public ToyModel_R_7(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void setupCagForR(InPVactConsumerAgentGroup cag) {
        super.setupCagForR(cag);
        cag.setA7(dirac01);
        cag.setA8(dirac01);
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        setDefaultWeights(mpm);
    }
}
