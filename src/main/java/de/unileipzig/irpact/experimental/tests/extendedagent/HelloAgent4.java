package de.unileipzig.irpact.experimental.tests.extendedagent;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class HelloAgent4 extends AbstractAgentLC {

    public HelloAgent4() {
        name = "Hello4";
    }

    @Override
    protected void onInit() {
        log("onInit " + (agent != null));
    }

    @Override
    protected void onStart() {
        log("onStart");
    }

    @Override
    protected void onEnd() {
        log("onEnd");
    }
}
