package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.components.core.EvaluationModule;
import de.unileipzig.irpact.core.process.PostAction;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentEvaluationModule extends EvaluationModule<ConsumerAgentData>, ConsumerAgentModule {

    @Override
    default AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        return evaluate(data, null);
    }

    @Override
    AdoptionResult evaluate(ConsumerAgentData input, List<PostAction<?>> postActions) throws Throwable;
}
