package de.unileipzig.irpact.experimental.tests.manyThreads;

import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
@Agent
public class ThreadAgent {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private boolean debug = false;
    private String name;
    @SuppressWarnings("FieldMayBeFinal")
    private RetData threadNames = new RetData();
    //private List<String> threadNames = new ArrayList<>();

    public ThreadAgent() {
    }

    //=========================
    //help
    //=========================

    private boolean is(int i) {
        return name.endsWith("#" + i);
    }

    private void log(String msg) {
        log(debug, msg);
    }

    private void log(boolean debug, String msg) {
        if(debug) {
            System.out.println("[" + LocalTime.now() + "]"
                    + " [" + name + "]"
                    + " " + msg);
        }
    }

    private void doStuff() {
        threadNames.add(threadName());
    }

    private String threadName() {
        return Thread.currentThread().toString();
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        log("onInit");
        threadNames.add(threadName());
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        threadNames.add(threadName());

        /*
        if(is(0)) {
            execFeature.waitForDelay(10, access -> {
                log(true, "START");
                return IFuture.DONE;
            });
        }

        execFeature.waitForDelay(10, access -> {
            Random r = new Random();
            for(int i = 0; i < 1000; i++) {
                long sleep = r.nextInt(1000) + 100;
                int _i = i;
                if(is(0) && _i == 0) {
                    log(true, "START: " + sleep);
                }
                execFeature.waitForDelay(sleep, access2 -> {
                    doStuff();
                    if(is(0) && _i == 0) {
                        log(true, "FINAL");
                    }
                    return IFuture.DONE;
                });
            }
            return IFuture.DONE;
        });
        */
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
        threadNames.add(threadName());
        resultsFeature.getResults().put("threadNames", threadNames);
    }
}
