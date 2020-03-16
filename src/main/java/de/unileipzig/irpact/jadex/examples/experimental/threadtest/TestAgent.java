package de.unileipzig.irpact.jadex.examples.experimental.threadtest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;

import java.util.HashMap;
import java.util.Map;

@Agent
public class TestAgent {

    public static final Map<String, Integer> counter = new HashMap<>();

    @Agent
    protected IInternalAccess agent;

    private void addToMap(String name) {
        synchronized (counter) {
            int current = counter.computeIfAbsent(name, _name -> 0);
            counter.put(name, current + 1);
        }
    }

    @OnStart
    public void onStart() {
        Thread current = Thread.currentThread();
        String name = current.getName();
        addToMap(name);
        //ConcurrentUtil.sleepSilently(100); //kuenstliche wartezeit
        //agent.killComponent();
    }
}
