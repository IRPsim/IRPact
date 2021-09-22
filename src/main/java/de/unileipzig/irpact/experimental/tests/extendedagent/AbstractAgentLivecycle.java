package de.unileipzig.irpact.experimental.tests.extendedagent;

import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAgentLivecycle {

    protected String name;

    public AbstractAgentLivecycle() {
    }

    protected void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    @OnInit
    protected abstract void onInit();

    @OnStart
    protected abstract void onStart();

    @OnEnd
    protected abstract void onEnd();
}
