package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ToyModeltModularProcessModelTemplate;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class ToyModel_R_5 extends ToyModel_R_4 {

    public static final int REVISION = 0;

    public ToyModel_R_5(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, resultConsumer);
        setRevision(REVISION);
    }

    @Override
    protected void customCagSetup(InPVactConsumerAgentGroup cag) {
        super.customCagSetup(cag);
        cag.setC1(dirac01);
        cag.setB6(dirac01);
    }

    @Override
    protected void customModuleSetup(ToyModeltModularProcessModelTemplate mpm) {
        setDefaultWeights(mpm);
    }
}
