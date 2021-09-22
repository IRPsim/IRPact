package de.unileipzig.irpact.experimental.tests.extendedagent;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.service.annotation.OnInit;
import jadex.micro.annotation.Agent;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class HelloAgent2 implements AgentInterface {

    protected String name;

    public HelloAgent2() {
        name = "Hello2";
    }

    protected void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    @Override
    @OnInit //dann gehts...
    public void onInit() {
        log("onInit");
    }

    @Override
    public void onStart() {
        log("onStart");
    }

    @Override
    public void onEnd() {
        log("onEnd");
    }
}
