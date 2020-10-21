package de.unileipzig.irpact.experimental.tests.extendedagent;

/**
 * @author Daniel Abitz
 */
public class HelloAgent extends AbstractAgent {

    public HelloAgent() {
        name = "Hello1";
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
