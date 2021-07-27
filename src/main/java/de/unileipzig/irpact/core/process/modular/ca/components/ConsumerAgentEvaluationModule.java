package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.components.core.EvaluationModule;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentEvaluationModule extends EvaluationModule<ConsumerAgentData>, ConsumerAgentModule {

    @Override
    AdoptionResult evaluate(ConsumerAgentData data) throws Throwable;
}
