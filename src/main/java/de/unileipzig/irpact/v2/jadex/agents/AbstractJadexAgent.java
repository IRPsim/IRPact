package de.unileipzig.irpact.v2.jadex.agents;

import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

/**
 * @author Daniel Abitz
 */
@Agent
public abstract class AbstractJadexAgent extends AbstractAgentBase {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    public AbstractJadexAgent() {
    }

    protected void initData() {
        initData(resultsFeature.getArguments());
    }
}
