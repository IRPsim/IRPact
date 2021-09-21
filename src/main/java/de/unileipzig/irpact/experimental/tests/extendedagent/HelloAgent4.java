package de.unileipzig.irpact.experimental.tests.extendedagent;

import jadex.bdiv3.BDIAgentFactory;
import jadex.micro.annotation.Agent;

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
