package de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate;

import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.AbstractReevaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DecisionMakingReevaluator
        extends AbstractReevaluator<ConsumerAgentData2>
        implements RAHelperAPI2 {

    protected List<Module2<ConsumerAgentData2, ?>> modules;

    public DecisionMakingReevaluator() {
        this(new ArrayList<>());
    }

    public DecisionMakingReevaluator(List<Module2<ConsumerAgentData2, ?>> modules) {
        this.modules = modules;
    }

    public void addModule(Module2<ConsumerAgentData2, ?> module) {
        modules.add(module);
    }

    @Override
    public SharedModuleData getSharedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        if(input.getStage() == RAStage2.IMPEDED) {
            trace("[{}] reset process stage '{}' to '{}' for agent '{}' and reevaluate", getName(), RAStage.IMPEDED, RAStage.DECISION_MAKING, input.getAgentName());
            input.setStage(RAStage2.DECISION_MAKING);
            for(Module2<ConsumerAgentData2, ?> module: modules) {
                module.apply(input, actions);
            }
        }
    }
}
